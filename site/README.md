# MãeLink — portal web hospitalar

Leia `../LEIA_PRIMEIRO.md`, `../README_CADASTRO_HOSPITAIS.md` e `../documentacao/SEGURANCA_E_IMPLANTACAO.md` antes de operar.

Requer Node.js >=22.16.0. Sem instalação de pacotes npm; o mapa regional é renderizado em SVG no próprio navegador, sem Leaflet, CDN ou tiles externos.

```powershell
cd site
node server.js
```

Abrir `http://127.0.0.1:3000`. Para criar a conta ADMIN, configure as variáveis no **servidor**, usando sua própria identidade institucional e senha forte:

```powershell
$env:ADMIN_EMAIL='admin@SEU-DOMINIO-INSTITUCIONAL'
$env:ADMIN_PASSWORD='DEFINA_UMA_SENHA_LONGA_UNICA'
node server.js
```

Para testes: `npm test`. A integração OpenAI é opcional com `OPENAI_API_KEY` **somente no servidor**; testes da integração usam mock da API e não comprovam funcionamento com uma chave real.

Dados ficam no caminho `DB_PATH` (por padrão `site/data/maelink.sqlite`); **não envie o banco SQLite, arquivos `.env` reais, senhas nem logs contendo dados pessoais para GitHub**. Hospitais não são parceiros por padrão. Não use dados reais de pacientes sem homologação institucional.


## Ajustes de navegação desta revisão

- Diretório histórico SP exibe quatro opções de início, com opção **Ver mais**. Geolocalização é opcional, exige autorização do navegador e requer HTTPS no domínio público. Distâncias são aproximadas e só para parceiros com coordenadas confirmadas.
- Os 27 links de estados consultam a rede oficial; não equivalem a convênio nem a credenciais hospitalares.
- Assistente sem `OPENAI_API_KEY`: oferece respostas locais limitadas e explicitamente identificadas como orientação automática, sem fingir ser IA. Com chave de API, exige testes adicionais com integração real.
- Inventário de hospitais nesta distribuição: **zero hospitais credenciados e zero credenciais institucionais válidas**. Senhas nunca são armazenadas em texto puro e não devem ser incluídas no README.
- Revisão técnica: `../RELATORIO_QA_NOVA_VERSAO.md`.

## Revisão final / salvaguardas

- Rode `npm test`: testes de API, isolamentos, formulários, autorização, chat com API externa simulada e backup.
- `npm run backup -- /caminho/seguro/copia.sqlite` realiza cópia consistente e checa integridade (Node >=22.16); não sobrescreve backup existente. Agende externamente, criptografe, limite acesso e teste restauração em ambiente isolado.
- `HOSPITAL_TIME_ZONE` usa `America/Sao_Paulo` por padrão. A implantação em hospitais de fusos distintos exige evolução do sistema para fuso por unidade; não habilite agendas multi-fuso antes disso.
- A permissão de localização é gerenciada pelo **navegador**, não por popup feito pelo site; requer HTTPS ou localhost. Se permissão anterior foi negada/concedida, altere no ícone de permissões do navegador.
- Testes automatizados não substituem homologação por hospitais, pentest, acessibilidade em navegador real, revisão clínica/LGPD e testes com API OpenAI autorizada.
