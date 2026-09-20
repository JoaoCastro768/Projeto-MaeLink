'use strict';
const test=require('node:test');
const assert=require('node:assert/strict');
const fs=require('node:fs');const os=require('node:os');const path=require('node:path');
const temporary=fs.mkdtempSync(path.join(os.tmpdir(),'maelink-hospital-v3-'));
process.env.DB_PATH=path.join(temporary,'db.sqlite');process.env.ADMIN_EMAIL='admin@hospitais.test';process.env.ADMIN_PASSWORD='senha-admin-de-teste-2026';delete process.env.OPENAI_API_KEY;
const {createServer,db,passwordHash,passwordMatches}=require('../server');
const srv=createServer();let base,admin,mae1,mae2,enfermeira,gestor,unit,otherUnit,slot,booking;
const future=()=>new Date(Date.now()+3*86400000).toISOString().slice(0,10);
const profile=(email,extra={})=>({email,password:'senha-forte-mãe-2026',full_name:'Maria Oliveira Teste',dob:'1994-03-19',phone:'(11) 98888-7777',city:'São Paulo',uf:'SP',baby_first_name:'Lucas',baby_dob:'2026-01-02',baby_sex:'NAO_INFORMAR',breastfeeding:'SIM',surplus:'SIM',privacy_consent:true,...extra});
async function req(route,{method='GET',body,cookie,headers={}}={}){const r=await fetch(base+route,{method,headers:{'X-MaeLink-Request':'1',...(body?{'Content-Type':'application/json'}:{}),...(cookie?{cookie}:{}),...headers},body:body?JSON.stringify(body):undefined});let data;try{data=await r.json();}catch{data=null;}return {status:r.status,data,headers:r.headers,cookie:r.headers.get('set-cookie')?.split(';')[0]};}
async function login(email,password){const r=await req('/api/login',{method:'POST',body:{email,password}});assert.equal(r.status,200,JSON.stringify(r.data));return r.cookie;}
test.before(async()=>{await new Promise(resolve=>srv.listen(0,'127.0.0.1',resolve));base='http://127.0.0.1:'+srv.address().port;});
test.after(async()=>{await new Promise(resolve=>srv.close(resolve));db.close();fs.rmSync(temporary,{recursive:true,force:true});});
test('01 páginas públicas, visual e políticas de segurança',async()=>{for(const file of ['/','/entrar.html','/cadastro.html','/minha-conta.html','/assistente.html','/styles.css','/portal.js','/assets/logo.svg']){const r=await fetch(base+file);assert.equal(r.status,200,file);assert.ok(r.headers.get('content-security-policy').includes("script-src 'self'"));}const home=await(await fetch(base+'/')).text();assert.match(home,/Uma gota de leite/);assert.doesNotMatch(home,/horários fictícios|UNIDADE FICTÍCIA/i);const style=await(await fetch(base+'/styles.css')).text();assert.match(style,/#eaf5ff/i);assert.match(style,/@media\(max-width:720px\)/);});
test('02 sem rede conveniada não se inventam hospitais nem horários',async()=>{const r=await req('/api/v2/hospitals');assert.equal(r.status,200);assert.deepEqual(r.data.hospitals,[]);assert.match(r.data.official_directory,/gov\.br/);assert.equal((await req('/api/v2/health')).data.hospitals,0);assert.equal((await req('/api/banks')).status,410);});
test('03 cadastro validado, proteção da criança e papéis sem escalação',async()=>{for(const changes of [{privacy_consent:false},{password:'curta'},{dob:'2012-01-01'},{baby_dob:'2039-01-01'},{baby_first_name:''}]){assert.equal((await req('/api/v2/register',{method:'POST',body:profile('invalida@teste.com',changes)})).status,400);}const r=await req('/api/v2/register',{method:'POST',body:profile('mae1@example.test',{role:'ADMIN'})});assert.equal(r.status,201,JSON.stringify(r.data));assert.equal((await req('/api/v2/register',{method:'POST',body:profile('mae1@example.test')})).status,409);mae1=await login('mae1@example.test','senha-forte-mãe-2026');const me=await req('/api/v2/me',{cookie:mae1});assert.equal(me.data.user.role,'DOADORA');assert.equal(me.data.profile.baby_first_name,'Lucas');assert.ok(me.headers.get('cache-control').includes('no-store'));});
test('04 segunda mãe com conta própria e isolamento de perfis',async()=>{assert.equal((await req('/api/v2/register',{method:'POST',body:profile('mae2@example.test',{full_name:'Joana Teste Alves',phone:'(21) 97777-6666',city:'Rio de Janeiro',uf:'RJ'})})).status,201);mae2=await login('mae2@example.test','senha-forte-mãe-2026');assert.equal((await req('/api/v2/me',{cookie:mae2})).data.profile.full_name,'Joana Teste Alves');assert.equal((await req('/api/v2/appointments',{cookie:mae2})).data.appointments.length,0);assert.equal((await req('/api/v2/admin/hospitals',{cookie:mae1})).status,403);});
test('05 admin criado somente via ambiente e acesso protegido',async()=>{admin=await login('admin@hospitais.test',process.env.ADMIN_PASSWORD);const s=await req('/api/v2/me',{cookie:admin});assert.equal(s.data.user.role,'ADMIN');assert.equal((await req('/api/v2/appointments')).status,401);assert.equal((await req('/api/v2/hospitals',{method:'POST',cookie:mae1,body:{name:'Teste'}})).status,403);});
test('06 hospital pendente não é publicado nem recebe agenda',async()=>{const body={name:'Hospital Parceiro de Testes',city:'São Paulo',uf:'SP',address:'Avenida de Testes 100, São Paulo',public_phone:'(11) 3333-4444',public_email:'contato@hospitais.test'};const r=await req('/api/v2/hospitals',{method:'POST',cookie:admin,body});assert.equal(r.status,201);unit=r.data.id;assert.equal((await req('/api/v2/hospitals')).data.hospitals.length,0);assert.equal((await req('/api/v2/hospitals/'+unit+'/approve',{method:'POST',cookie:admin,body:{parceria_confirmada:false,agenda_autorizada:true}})).status,400);assert.equal((await req('/api/v2/slots',{method:'POST',cookie:admin,body:{hospital_id:unit,day:future(),hour:'10:30',capacity:1}})).status,400);});
test('07 ativação depende de duas declarações explícitas de verificação',async()=>{let r=await req('/api/v2/hospitals/'+unit+'/approve',{method:'POST',cookie:admin,body:{parceria_confirmada:true,agenda_autorizada:true}});assert.equal(r.status,200);r=await req('/api/v2/hospitals');assert.equal(r.data.hospitals.length,1);assert.equal(r.data.hospitals[0].name,'Hospital Parceiro de Testes');assert.equal(r.data.hospitals[0].approved,undefined);assert.equal((await req('/api/v2/hospitals/'+unit+'/approve',{method:'POST',cookie:mae1,body:{parceria_confirmada:true,agenda_autorizada:true}})).status,403);});
test('08 profissionais precisam de vínculo hospitalar concedido por administrador',async()=>{let r=await req('/api/v2/team',{method:'POST',cookie:mae1,body:{email:'nurse@hospitais.test',password:'senha-nurse-2026',role:'ATENDENTE',hospital_id:unit}});assert.equal(r.status,403);for(const role of ['ADMIN','DOADORA'])assert.equal((await req('/api/v2/team',{method:'POST',cookie:admin,body:{email:role+'@hospitais.test',password:'senha-ruim',role,hospital_id:unit}})).status,400);r=await req('/api/v2/team',{method:'POST',cookie:admin,body:{email:'nurse@hospitais.test',password:'senha-nurse-2026',role:'ATENDENTE',hospital_id:unit}});assert.equal(r.status,201);r=await req('/api/v2/team',{method:'POST',cookie:admin,body:{email:'manager@hospitais.test',password:'senha-manager-2026',role:'GESTOR',hospital_id:unit}});assert.equal(r.status,201);enfermeira=await login('nurse@hospitais.test','senha-nurse-2026');gestor=await login('manager@hospitais.test','senha-manager-2026');assert.equal((await req('/api/v2/me',{cookie:enfermeira})).data.memberships[0].id,unit);});
test('09 agenda publicada somente por gestor ou administrador vinculado',async()=>{const data={hospital_id:unit,day:future(),hour:'10:30',capacity:1};assert.equal((await req('/api/v2/slots',{method:'POST',cookie:enfermeira,body:data})).status,403);const r=await req('/api/v2/slots',{method:'POST',cookie:gestor,body:data});assert.equal(r.status,201);slot=r.data.id;assert.equal((await req('/api/v2/slots',{method:'POST',cookie:gestor,body:data})).status,409);const q=await req(`/api/v2/slots?hospitalId=${unit}&day=${future()}`);assert.equal(q.data.slots.length,1);assert.equal(q.data.slots[0].free,1);});
test('10 não há confirmação automática: mãe solicita horário publicado',async()=>{const r=await req('/api/v2/appointments',{method:'POST',cookie:mae1,body:{slot_id:slot,status:'CONCLUIDO',user_id:88}});assert.equal(r.status,201,JSON.stringify(r.data));assert.equal(r.data.appointment.status,'SOLICITADO');booking=r.data.appointment.id;const list=await req('/api/v2/appointments',{cookie:mae1});assert.equal(list.data.appointments.length,1);assert.equal(list.data.appointments[0].mother_phone,undefined);assert.equal((await req(`/api/v2/slots?hospitalId=${unit}&day=${future()}`)).data.slots.length,0);});
test('11 lotação impede reserva simultânea e duplicidade',async()=>{assert.equal((await req('/api/v2/appointments',{method:'POST',cookie:mae2,body:{slot_id:slot}})).status,409);assert.equal((await req('/api/v2/appointments',{method:'POST',cookie:mae1,body:{slot_id:slot}})).status,409);});
test('12 mãe não acessa ou altera atendimentos de outra mãe',async()=>{assert.equal((await req('/api/v2/appointments/'+booking+'/confirm',{method:'POST',cookie:mae1,body:{}})).status,403);assert.equal((await req('/api/v2/appointments/'+booking+'/cancel',{method:'POST',cookie:mae2,body:{}})).status,403);assert.equal((await req('/api/v2/donations',{cookie:mae2})).data.donations.length,0);});
test('13 enfermagem vê somente pacientes da sua unidade',async()=>{const q=await req('/api/v2/appointments?hospitalId='+unit,{cookie:enfermeira});assert.equal(q.status,200);assert.equal(q.data.appointments.length,1);assert.equal(q.data.appointments[0].mother_name,'Maria Oliveira Teste');assert.ok(q.data.appointments[0].mother_phone);});
test('14 conclusão só ocorre após confirmação profissional',async()=>{assert.equal((await req('/api/v2/appointments/'+booking+'/donation',{method:'POST',cookie:enfermeira,body:{volume_ml:150}})).status,409);const c=await req('/api/v2/appointments/'+booking+'/confirm',{method:'POST',cookie:enfermeira,body:{}});assert.equal(c.status,200);assert.equal((await req('/api/v2/appointments/'+booking+'/donation',{method:'POST',cookie:mae1,body:{volume_ml:150}})).status,403);});
test('15 histórico individual somente após registro da equipe; sem duplicidade',async()=>{const r=await req('/api/v2/appointments/'+booking+'/donation',{method:'POST',cookie:enfermeira,body:{volume_ml:150}});assert.equal(r.status,201,JSON.stringify(r.data));assert.equal((await req('/api/v2/appointments/'+booking+'/donation',{method:'POST',cookie:enfermeira,body:{volume_ml:150}})).status,409);const h=await req('/api/v2/donations',{cookie:mae1});assert.equal(h.data.donations.length,1);assert.equal(h.data.donations[0].volume_ml,150);assert.equal((await req('/api/v2/donations',{cookie:mae2})).data.donations.length,0);});
test('16 unidade estranha não tem acesso a registros de outra unidade',async()=>{const r=await req('/api/v2/hospitals',{method:'POST',cookie:admin,body:{name:'Hospital Independente de Testes',city:'Recife',uf:'PE',address:'Rua Testes 200, Recife',public_phone:'(81) 3333-2222'}});otherUnit=r.data.id;await req('/api/v2/hospitals/'+otherUnit+'/approve',{method:'POST',cookie:admin,body:{parceria_confirmada:true,agenda_autorizada:true}});assert.equal((await req('/api/v2/appointments?hospitalId='+otherUnit,{cookie:enfermeira})).status,403);assert.equal((await req('/api/v2/appointments?hospitalId='+otherUnit,{cookie:gestor})).status,403);assert.equal((await req('/api/v2/slots',{method:'POST',cookie:gestor,body:{hospital_id:otherUnit,day:future(),hour:'12:00',capacity:1}})).status,400);});
test('17 cancelamento devolve vaga e mantém trilha do pedido',async()=>{const sl=await req('/api/v2/slots',{method:'POST',cookie:gestor,body:{hospital_id:unit,day:future(),hour:'11:00',capacity:1}});const id=sl.data.id;const book=await req('/api/v2/appointments',{method:'POST',cookie:mae2,body:{slot_id:id}});assert.equal(book.status,201);const cancel=await req(`/api/v2/appointments/${book.data.appointment.id}/cancel`,{method:'POST',cookie:mae2,body:{}});assert.equal(cancel.status,200);assert.equal((await req(`/api/v2/slots?hospitalId=${unit}&day=${future()}`)).data.slots.find(x=>x.id===id).free,1);});
test('18 atualização do perfil e integridade do histórico',async()=>{const data=profile('mae1@example.test',{phone:'(11) 90000-1234',baby_first_name:'Luca'});const r=await req('/api/v2/profile',{method:'PATCH',cookie:mae1,body:data});assert.equal(r.status,200);const updated=await req('/api/v2/me',{cookie:mae1});assert.equal(updated.data.profile.baby_first_name,'Luca');assert.equal((await req('/api/v2/donations',{cookie:mae1})).data.donations.length,1);});
test('19 API não expõe dados clínicos e mantém controles básicos',async()=>{let r=await req('/api/v2/me');assert.equal(r.status,401);r=await req('/api/v2/appointments',{method:'POST',cookie:mae1,headers:{Origin:'https://attacker.example'},body:{slot_id:slot}});assert.equal(r.status,403);assert.equal((await req('/api/team',{cookie:admin})).status,410);assert.equal((await req('/api/v2/health')).data.ai_configured,false);const page=await(await fetch(base+'/portal.js')).text();assert.ok(!page.includes('OPENAI_API_KEY'));assert.ok(!page.includes('localStorage'));});
test('20 assistente: dados pessoais recusados, clínica encaminhada, chave ausente não finge IA',async()=>{const pii=await req('/api/v2/chat',{method:'POST',body:{ai_consent:true,question:'Meu CPF é 123.456.789-10 como doar?'}});assert.equal(pii.status,422);const health=await req('/api/v2/chat',{method:'POST',body:{ai_consent:true,question:'Meu bebê está com febre, posso doar?'}});assert.equal(health.status,200);assert.equal(health.data.mode,'encaminhamento');const noKey=await req('/api/v2/chat',{method:'POST',body:{ai_consent:true,question:'Como posso começar a doar leite humano?'}});assert.equal(noKey.status,200);assert.equal(noKey.data.mode,'orientacao_local');assert.match(noKey.data.answer,/banco de leite/i);});
test('21 integração OpenAI preparada para moderação e resposta sem armazenamento (mock externo)',async()=>{const original=globalThis.fetch;process.env.OPENAI_API_KEY='chave-exclusivamente-teste';let steps=[];globalThis.fetch=async(url,opts)=>{if(String(url).startsWith('https://api.openai.com/')){steps.push({url,body:JSON.parse(opts.body),headers:opts.headers});if(String(url).endsWith('/moderations'))return new Response(JSON.stringify({results:[{flagged:false}]}),{status:200});return new Response(JSON.stringify({output:[{content:[{type:'output_text',text:'Procure um banco de leite para orientação geral.'}]}]}),{status:200});}return original(url,opts);};try{const r=await req('/api/v2/chat',{method:'POST',body:{ai_consent:true,question:'Onde encontro um banco de leite próximo?'}});assert.equal(r.status,200,JSON.stringify(r.data));assert.equal(r.data.mode,'ia');assert.equal(steps.length,3);assert.equal(steps[1].body.store,false);assert.equal(steps[1].body.max_output_tokens,320);assert.equal(steps[1].body.input,'Onde encontro um banco de leite próximo?');assert.ok(!JSON.stringify(steps).includes('Maria Oliveira'));}finally{globalThis.fetch=original;delete process.env.OPENAI_API_KEY;}});
test('22 senha criptografada e sessão expira no logout',async()=>{const digest=passwordHash('testPassword#2026');assert.ok(digest.startsWith('scrypt$'));assert.ok(passwordMatches('testPassword#2026',digest));assert.ok(!passwordMatches('badPassword',digest));const logout=await req('/api/logout',{method:'POST',cookie:mae1});assert.equal(logout.status,200);assert.equal((await req('/api/v2/me',{cookie:mae1})).status,401);});

test('23 diretório SP: até 50 referências históricas não ativam agendas nem parceiros',async()=>{
 const r=await req('/api/v2/directory/sp');assert.equal(r.status,200);assert.equal(r.data.units.length,50);
 assert.equal(r.data.source_year,2016);assert.ok(r.data.source.includes('saude.sp.gov.br'));
 assert.ok(r.data.units.every(x=>x.uf==='SP' && x.name && x.city && !('id' in x)));
 assert.equal((await req('/api/v2/hospitals')).data.hospitals.length,2);
});
test('24 pedido de hospital: formulário público não cria parceria automaticamente',async()=>{
 const body={name:'Hospital Pedido de Teste',cnpj:'11.222.333/0001-81',city:'Campinas',uf:'SP',address:'Rua das Flores 400, Campinas',contact_name:'Fernanda Teste',contact_email:'responsavel@hospital.test',contact_phone:'(19) 3222-7777',confirm_truth:true};
 assert.equal((await req('/api/v2/hospital-applications',{method:'POST',body:{...body,confirm_truth:false}})).status,400);
 const r=await req('/api/v2/hospital-applications',{method:'POST',body});assert.equal(r.status,201,JSON.stringify(r.data));assert.match(r.data.message,/avaliação administrativa/);
 assert.equal((await req('/api/v2/admin/applications')).status,401);
 assert.equal((await req('/api/v2/admin/applications',{cookie:mae2})).status,403);
 const pending=await req('/api/v2/admin/applications',{cookie:admin});assert.equal(pending.status,200);
 const found=pending.data.applications.find(x=>x.id===r.data.id);assert.equal(found.status,'PENDENTE');
 assert.equal((await req('/api/v2/hospitals')).data.hospitals.some(x=>x.name===body.name),false);
 const review=await req('/api/v2/admin/applications/'+r.data.id+'/review',{method:'POST',cookie:admin,body:{status:'APROVADA'}});assert.equal(review.status,200);
 assert.equal((await req('/api/v2/hospitals')).data.hospitals.some(x=>x.name===body.name),false);
});
test('25 solicitação institucional não pode ser revisada por mãe nem por status inventado',async()=>{
 const r=await req('/api/v2/admin/applications',{cookie:admin});const id=r.data.applications[0].id;
 assert.equal((await req('/api/v2/admin/applications/'+id+'/review',{method:'POST',cookie:mae2,body:{status:'RECUSADA'}})).status,403);
 assert.equal((await req('/api/v2/admin/applications/'+id+'/review',{method:'POST',cookie:admin,body:{status:'AUTO_ATIVAR'}})).status,400);
});
test('26 coordenadas: pares obrigatórios, limites geográficos e unidade pendente',async()=>{
 const data={name:'Unidade Teste Coordenada',city:'São Paulo',uf:'SP',address:'Rua de Testes 500, São Paulo',public_phone:'(11) 3666-5555'};
 assert.equal((await req('/api/v2/hospitals',{method:'POST',cookie:admin,body:{...data,latitude:-23.5}})).status,400);
 assert.equal((await req('/api/v2/hospitals',{method:'POST',cookie:admin,body:{...data,latitude:900,longitude:900}})).status,400);
 const created=await req('/api/v2/hospitals',{method:'POST',cookie:admin,body:{...data,latitude:-23.55,longitude:-46.63}});
 assert.equal(created.status,201);assert.equal((await req('/api/v2/hospitals')).data.hospitals.some(h=>h.id===created.data.id),false);
});
test('27 novas telas e URLs disponíveis sem expor dados administrativos',async()=>{
 for(const name of ['cadastro-concluido.html','configuracoes.html','hospital-cadastro.html','solicitacao-concluida.html']){
  const response=await fetch(base+'/'+name);assert.equal(response.status,200,name);
  const page=await response.text();assert.match(page,/MãeLink/);assert.ok(!page.includes('ADMIN_PASSWORD'));
 }
 const site=await(await fetch(base+'/')).text();assert.match(site,/reference-list/);assert.match(site,/hospital-map/);assert.match(site,/hospital-cadastro.html/);assert.match(site,/help-launcher/);
 const form=await(await fetch(base+'/cadastro.html')).text();assert.match(form,/novalidate/);assert.match(form,/register-form/);
 const client=await(await fetch(base+'/portal.js')).text();assert.match(client,/validateRegistrationField/);assert.match(client,/solicitacao-concluida.html/);
});

// QA regressiva: diretório, localização opcional e assistente com orientação local.
test('28 referências: quatro opções iniciais e botão ver mais, sem seletor de quantidade',async()=>{
 const page=await(await fetch(base+'/')).text();
 assert.ok(page.includes('id="reference-more"'));
 assert.ok(page.includes('id="find-nearby"'));
 assert.ok(!page.includes('id="reference-limit"'));
 assert.ok(page.includes('id="hospital-map"'));
 const js=await(await fetch(base+'/portal.js')).text();
 assert.ok(js.includes('slice(0,expanded?50:4)'));
 assert.ok(js.includes('navigator.geolocation.getCurrentPosition'));
 assert.ok(js.includes('distanceKm('));
 assert.ok(!js.includes('tile.openstreetmap.org'));
});
test('29 assistente devolve respostas locais honestas para dúvidas usuais sem chave',async()=>{
 for(const [question,pattern] of [
  ['Como faço para agendar uma coleta?',/horário/i],
  ['Onde encontro um banco de leite?',/unidade/i],
  ['Como faço para armazenar leite?',/banco de leite/i],
  ['Como posso mudar meus dados de cadastro?',/Configurações/i],
 ]){
  const r=await req('/api/v2/chat',{method:'POST',body:{ai_consent:true,question}});
  assert.equal(r.status,200,JSON.stringify(r.data));assert.equal(r.data.mode,'orientacao_local');assert.match(r.data.answer,pattern);
  assert.equal(r.data.source,'https://www.gov.br/saude/pt-br/assuntos/saude-de-a-a-z/d/doacao-de-leite');
 }
});
test('30 preserva autorização, rejeita informações identificadoras no assistente',async()=>{
 assert.equal((await req('/api/v2/chat',{method:'POST',body:{ai_consent:false,question:'Como posso doar leite humano?'}})).status,400);
 assert.equal((await req('/api/v2/chat',{method:'POST',body:{ai_consent:true,question:'Envio meu e-mail maria@exemplo.com para atendimento'}})).status,422);
});

// QA adicional — testes que reproduzem falhas descobertas durante a auditoria.
test('31 navegador pode solicitar permissão NATIVA de geolocalização',async()=>{
 const r=await fetch(base+'/');
 assert.equal(r.status,200);
 const policy=r.headers.get('permissions-policy');
 assert.match(policy,/geolocation=\(self\)/);
 assert.doesNotMatch(policy,/geolocation=\(\)/);
 const js=await (await fetch(base+'/portal.js')).text();
 assert.match(js,/navigator\.geolocation\.getCurrentPosition/);
});
test('32 cadastro rejeita consentimento não booleano e datas impossíveis',async()=>{
 for (const badInput of [
  {privacy_consent:'false'},
  {privacy_consent:1},
  {baby_dob:'1980-01-01'},
  {baby_dob:'2026-02-30'},
  {baby_first_name:'<script>alert(1)</script>'},
  {dob:'2000-02-30'}
 ]){
  const r=await req('/api/v2/profile',{method:'PATCH',cookie:mae2,body:profile('mae2@example.test',badInput)});
  assert.equal(r.status,400,JSON.stringify(badInput));
 }
});
test('33 atualização de perfil não aceita consentimento falso disfarçado',async()=>{
 const r=await req('/api/v2/profile',{method:'PATCH',cookie:mae2,body:profile('mae2@example.test',{privacy_consent:'false'})});
 assert.equal(r.status,400);
});
test('34 HEAD devolve cabeçalhos mas nenhum corpo; caminho malformado não causa 500',async()=>{
 const head=await fetch(base+'/portal.js',{method:'HEAD'});
 assert.equal(head.status,200);
 assert.equal((await head.arrayBuffer()).byteLength,0);
 assert.ok(Number(head.headers.get('content-length'))>0);
 assert.equal((await fetch(base+'/%E0%A4%A')).status,400);
 assert.equal((await fetch(base+'/../../server.js')).status,404);
});
test('35 Unicode com múltiplos pedaços JSON permanece íntegro',async()=>{
 const raw=JSON.stringify({question:'Como faço para doar leite para bebês? 💙',ai_consent:true});
 const bytes=Buffer.from(raw);const i=bytes.indexOf(Buffer.from('💙'));
 assert.ok(i>0);
 const stream=new ReadableStream({start(c){c.enqueue(Uint8Array.from(bytes.subarray(0,i+1)));c.enqueue(Uint8Array.from(bytes.subarray(i+1)));c.close();}});
 const result=await fetch(base+'/api/v2/chat',{method:'POST',headers:{'Content-Type':'application/json','X-MaeLink-Request':'1'},body:stream,duplex:'half'});
 assert.equal(result.status,200,await result.text());
});
test('36 timezone hospitalar configurado não usa UTC para determinar o dia',async()=>{
 const source=fs.readFileSync(path.join(__dirname,'../care.js'),'utf8');
 assert.match(source,/America\/Sao_Paulo/);
 assert.match(source,/currentHour\(\)/);
 assert.doesNotMatch(source,/new Date\(\)\.toTimeString\(\)/);
 const env=fs.readFileSync(path.join(__dirname,'../.env.example'),'utf8');
 assert.match(env,/HOSPITAL_TIME_ZONE/);
});
test('37 backup consistente, restaurável e sem sobrescrever um arquivo existente',async()=>{
 const {spawnSync}=require('node:child_process');
 const source=process.env.DB_PATH;const out=path.join(temporary,'backups','snapshot.sqlite');
 const args=[path.join(__dirname,'../scripts/backup.mjs'),out];
 const run=spawnSync(process.execPath,args,{env:{...process.env,DB_PATH:source},encoding:'utf8',timeout:12000});
 assert.equal(run.status,0,run.stderr);
 assert.ok(fs.statSync(out).size>0);
 const {DatabaseSync}=require('node:sqlite');const copy=new DatabaseSync(out,{readOnly:true});
 assert.equal(copy.prepare('PRAGMA integrity_check').get().integrity_check,'ok');
 assert.ok(copy.prepare('SELECT count(*) AS n FROM users').get().n>=4);copy.close();
 const again=spawnSync(process.execPath,args,{env:{...process.env,DB_PATH:source},encoding:'utf8',timeout:12000});
 assert.equal(again.status,2,'Backup existente não deve ser sobrescrito');
});
test('38 não há chaves embutidas em arquivos públicos nem mapa dependente de OSM',async()=>{
 const p=path.join(__dirname,'../public');
 for(const f of fs.readdirSync(p,{withFileTypes:true}).filter(f=>f.isFile())){
  const content=fs.readFileSync(path.join(p,f.name),'utf8');
  assert.doesNotMatch(content,/sk-proj-[A-Za-z0-9_-]{20}/);
  assert.doesNotMatch(content,/tile\.openstreetmap\.org/);
 }
});
