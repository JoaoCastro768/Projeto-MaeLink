// Backup consistente do SQLite (WAL), fora da pasta pública.
// Uso: node scripts/backup.mjs /caminho/seguro/copia.sqlite
import {DatabaseSync, backup} from 'node:sqlite';
import fs from 'node:fs';
import path from 'node:path';
const source=path.resolve(process.env.DB_PATH || path.join(import.meta.dirname,'..','data','maelink.sqlite'));
const targetArg=process.argv[2];
if (!targetArg) {console.error('Uso: node scripts/backup.mjs CAMINHO_DESTINO.sqlite');process.exit(2);}
const target=path.resolve(targetArg);
if(source===target || !fs.existsSync(source) || fs.existsSync(target)){
 console.error('Fonte inexistente, destino igual à origem ou backup já existente.');process.exit(2);
}
fs.mkdirSync(path.dirname(target),{recursive:true,mode:0o700});
let connection;
try{
 connection=new DatabaseSync(source,{readOnly:true});
 await backup(connection,target);
 const copy=new DatabaseSync(target,{readOnly:true});
 const integrity=copy.prepare('PRAGMA integrity_check').get();
 copy.close();
 if(integrity.integrity_check!=='ok')throw Error('Falha de integridade');
 fs.chmodSync(target,0o600);
 console.log('Backup SQLite íntegro criado em: '+target);
}catch(e){try{fs.unlinkSync(target)}catch{}console.error('Backup não concluído: '+e.message);process.exitCode=1;}
finally{connection?.close();}
