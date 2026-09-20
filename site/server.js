'use strict';
const http = require('node:http');
const fs = require('node:fs');
const path = require('node:path');
const crypto = require('node:crypto');
const {DatabaseSync} = require('node:sqlite');
const {makeCare} = require('./care');

const root = __dirname;
// O navegador precisa poder solicitar a localização mediante autorização explícita.
// Política restritiva geolocation=() impedia o recurso, mesmo após clicar em permitir.
const publicDir = path.join(root,'public');
const dbFile = process.env.DB_PATH || path.join(root,'data','maelink.sqlite');
if (dbFile !== ':memory:') fs.mkdirSync(path.dirname(dbFile),{recursive:true,mode:0o700});
const db = new DatabaseSync(dbFile);
db.exec(`PRAGMA foreign_keys=ON; PRAGMA journal_mode=WAL;
 CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL UNIQUE, password_hash TEXT NOT NULL, role TEXT NOT NULL CHECK(role IN ('DOADORA','ATENDENTE','GESTOR','ADMIN')), created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP);
 CREATE TABLE IF NOT EXISTS sessions (token_hash TEXT PRIMARY KEY, user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE, expires_at INTEGER NOT NULL);
 CREATE TABLE IF NOT EXISTS requests (id TEXT PRIMARY KEY, user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE, bank_id TEXT NOT NULL, date TEXT NOT NULL, slot TEXT NOT NULL, status TEXT NOT NULL DEFAULT 'SOLICITADO' CHECK(status IN ('SOLICITADO','CONFIRMADO','CONCLUIDO','CANCELADO')), created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP);
 CREATE INDEX IF NOT EXISTS idx_requests_owner ON requests(user_id);`);

const sha256 = value=>crypto.createHash('sha256').update(value).digest('hex');
function passwordHash(password,salt=crypto.randomBytes(16).toString('hex')) {
  const derived=crypto.scryptSync(password,salt,64,{N:16384,r:8,p:1}).toString('hex');
  return `scrypt$${salt}$${derived}`;
}
function passwordMatches(password,stored){
  const parts=stored.split('$');
  if(parts.length!==3 || parts[0]!=='scrypt') return false;
  const given=Buffer.from(passwordHash(password,parts[1]).split('$')[2],'hex');
  const expected=Buffer.from(parts[2],'hex');
  return expected.length===given.length && crypto.timingSafeEqual(expected,given);
}
function validEmail(value){return typeof value==='string' && value.length<=254 && /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);}
function validPassword(value){return typeof value==='string' && value.length>=12 && value.length<=128;}
function normalizedEmail(email){return email.trim().toLowerCase();}
const adminEmail=process.env.ADMIN_EMAIL;
const adminPassword=process.env.ADMIN_PASSWORD;
if (adminEmail || adminPassword) {
  if (!validEmail(adminEmail) || !validPassword(adminPassword)) throw Error('ADMIN_EMAIL válido e ADMIN_PASSWORD com 12–128 caracteres são necessários.');
  if (!db.prepare("SELECT id FROM users WHERE role='ADMIN' LIMIT 1").get()) {
    db.prepare("INSERT INTO users(email,password_hash,role) VALUES(?,?,'ADMIN')").run(normalizedEmail(adminEmail),passwordHash(adminPassword));
    console.info('Conta administrativa inicial criada; remova ADMIN_PASSWORD das variáveis de ambiente após o primeiro uso.');
  }
}

const limits=new Map();
function limited(key,max,periodMs) {
  const now=Date.now(); const v=limits.get(key)||{count:0,until:now+periodMs};
  if(now>v.until){v.count=0;v.until=now+periodMs;}
  v.count++; limits.set(key,v);
  if(limits.size>20000) for (const [k,x] of limits) if(x.until<now) limits.delete(k);
  return v.count>max;
}
function send(res,status,data,headers={}){
  res.writeHead(status,{'Content-Type':'application/json; charset=utf-8','Cache-Control':'no-store',...headers});
  res.end(JSON.stringify(data));
}
const bad=(res,status,message)=>send(res,status,{error:message});
function cookieValue(req,name){
  const match=(req.headers.cookie||'').split(';').map(x=>x.trim()).find(x=>x.startsWith(name+'='));
  return match?match.slice(name.length+1):'';
}
function currentUser(req){
  const token=cookieValue(req,'ml_session');
  if(!/^[0-9a-f]{64}$/.test(token))return null;
  const row=db.prepare('SELECT u.id,u.email,u.role,s.expires_at FROM sessions s JOIN users u ON u.id=s.user_id WHERE s.token_hash=?').get(sha256(token));
  if(!row)return null;
  if(row.expires_at<Date.now()){db.prepare('DELETE FROM sessions WHERE token_hash=?').run(sha256(token));return null;}
  return {id:row.id,email:row.email,role:row.role};
}
function publicUser(user){return user?{email:user.email,role:user.role}:null;}
function sessionCookie(value,clear=false){return `ml_session=${value}; HttpOnly; SameSite=Strict; Path=/; ${process.env.COOKIE_SECURE==='true'?'Secure; ':''}Max-Age=${clear?0:28800}`;}
function createSession(res,user){
  const token=crypto.randomBytes(32).toString('hex');
  db.prepare('INSERT INTO sessions(token_hash,user_id,expires_at) VALUES(?,?,?)').run(sha256(token),user.id,Date.now()+28800000);
  send(res,200,{user:publicUser(user)},{'Set-Cookie':sessionCookie(token)});
}
function requireUser(req,res,roles=null){
  const user=currentUser(req);
  if(!user){bad(res,401,'Entre na sua conta para continuar.');return null;}
  if(roles&&!roles.includes(user.role)){bad(res,403,'Seu perfil não possui permissão para esta operação.');return null;}
  return user;
}
async function bodyJson(req){
  if(!(req.headers['content-type']||'').toLowerCase().startsWith('application/json'))throw Object.assign(Error('Envie JSON.'),{status:415});
  const chunks=[]; let bytes=0;
  for await(const chunk of req){bytes+=chunk.length;if(bytes>12_000)throw Object.assign(Error('Conteúdo muito grande.'),{status:413});chunks.push(chunk);}
  let parsed;
  try{parsed=JSON.parse(Buffer.concat(chunks).toString('utf8'));}catch{throw Object.assign(Error('JSON inválido.'),{status:400});}
  if(!parsed || typeof parsed!=='object' || Array.isArray(parsed))throw Object.assign(Error('JSON deve ser um objeto.'),{status:400});
  return parsed;
}
function requireWriteSecurity(req,res){
  if(!['POST','PATCH','PUT','DELETE'].includes(req.method))return true;
  const origin=req.headers.origin;
  const host=req.headers.host;
  // Origem do navegador + cabeçalho não simples reduzem CSRF; cookies são SameSite=Strict.
  let originAllowed=true;
  if(origin){try{const parsed=new URL(origin);originAllowed=Boolean(host && parsed.host===host && ['http:','https:'].includes(parsed.protocol));}catch{originAllowed=false;}}
  if(req.headers['x-maelink-request']!=='1'||!originAllowed){
    bad(res,403,'Requisição não autorizada.');return false;
  }
  return true;
}
function staticFile(req,res,pathname){
  const key=pathname==='/'?'/index.html':pathname;
  let rel;
  try {rel=decodeURIComponent(key);}catch{return bad(res,400,'Endereço inválido.');}
  if(rel.includes('..')||rel.includes('\\')||rel.includes('\0')){bad(res,404,'Não encontrado.');return;}
  const target=path.resolve(publicDir,'.'+rel);
  if(!target.startsWith(publicDir+path.sep)){bad(res,404,'Não encontrado.');return;}
  let st;
  try{st=fs.statSync(target)}catch{bad(res,404,'Não encontrado.');return;}
  if(!st.isFile()){bad(res,404,'Não encontrado.');return;}
  const ext=path.extname(target).toLowerCase();
  const types={'.html':'text/html; charset=utf-8','.css':'text/css; charset=utf-8','.js':'text/javascript; charset=utf-8','.svg':'image/svg+xml','.png':'image/png','.ico':'image/x-icon'};
  if(!types[ext]){bad(res,404,'Não encontrado.');return;}
  res.writeHead(200,{'Content-Type':types[ext],'Cache-Control':'no-cache','Content-Length':st.size});
  if(req.method==='HEAD'){res.end();return;}
  fs.createReadStream(target).pipe(res);
}
function futureDate(value){
  if(typeof value!=='string'||!/^\d{4}-\d{2}-\d{2}$/.test(value))return false;
  const stamp=Date.parse(value+'T12:00:00Z');
  const today=new Date();today.setUTCHours(0,0,0,0);
  return Number.isFinite(stamp)&&new Date(stamp).toISOString().slice(0,10)===value&&stamp>=today.getTime()&&stamp<=today.getTime()+1000*60*60*24*90;
}
const care=makeCare({db,send,bad,requireUser,bodyJson,limited,passwordHash});
async function handler(req,res){
  // Headers de defesa em profundidade; mapa e interface não carregam recursos externos.
  res.setHeader('X-Content-Type-Options','nosniff');
  res.setHeader('Referrer-Policy','no-referrer');
  res.setHeader('X-Frame-Options','DENY');
  res.setHeader('Permissions-Policy','camera=(), microphone=(), geolocation=(self)');
  res.setHeader('Content-Security-Policy',"default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; connect-src 'self'; base-uri 'none'; form-action 'self'; frame-ancestors 'none'");
  let url;
  try{url=new URL(req.url,'http://localhost');}catch{return bad(res,400,'URL inválida.');}
  const p=url.pathname;
  if(!requireWriteSecurity(req,res))return;
  if(p.startsWith('/api/v2/'))return care.route(req,res,p,url);
  if(req.method==='GET'&&p==='/api/session')return send(res,200,{user:publicUser(currentUser(req))});
  if(req.method==='POST'&&p==='/api/login'){
    const v=await bodyJson(req);
    if(!validEmail(v.email)||typeof v.password!=='string')return bad(res,401,'Credenciais inválidas.');
    const key='login:'+req.socket.remoteAddress+':'+sha256(normalizedEmail(v.email));
    if(limited(key,8,15*60000))return bad(res,429,'Muitas tentativas; aguarde 15 minutos.');
    const user=db.prepare('SELECT * FROM users WHERE email=?').get(normalizedEmail(v.email));
    // Comparação de senha até quando o usuário não existe, para reduzir diferenças observáveis.
    const fallback='scrypt$00000000000000000000000000000000$'+crypto.scryptSync('dummy','00000000000000000000000000000000',64).toString('hex');
    if(!passwordMatches(v.password,user?user.password_hash:fallback)||!user)return bad(res,401,'Credenciais inválidas.');
    return createSession(res,user);
  }
  if(req.method==='POST'&&p==='/api/logout'){
    const token=cookieValue(req,'ml_session');
    if(/^[0-9a-f]{64}$/.test(token))db.prepare('DELETE FROM sessions WHERE token_hash=?').run(sha256(token));
    return send(res,200,{ok:true},{'Set-Cookie':sessionCookie('',true)});
  }
  // O MVP anterior utilizava registros fictícios: rotas legadas não são publicadas.
  if(p.startsWith('/api/'))return bad(res,410,'Serviço anterior indisponível: utilize /api/v2.');
  if(req.method==='GET'||req.method==='HEAD')return staticFile(req,res,p);
  return bad(res,405,'Método não permitido.');
}
function createServer(){return http.createServer((req,res)=>{handler(req,res).catch(e=>{
  // Nunca retornar stack, credenciais ou dados da exceção ao cliente.
  if(!e.status || e.status>=500)console.error('Falha interna:',e.name);
  if(!res.headersSent)bad(res,e.status||500,e.status?e.message:'Ocorreu um erro interno.');else res.end();
});});}
if(require.main===module){const port=Number(process.env.PORT||3000);createServer().listen(port,'127.0.0.1',()=>console.log(`MãeLink portal em http://127.0.0.1:${port}`));}
module.exports={createServer,db,passwordHash,passwordMatches};
