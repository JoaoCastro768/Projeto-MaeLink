# Cadastro de hospitais e contas institucionais — MãeLink

## Inventário REAL desta distribuição

**Hospitais credenciados previamente: 0. Contas institucionais reais previamente emitidas: 0.** O ZIP não contém banco de dados com convênios, agendas nem logins emitidos por hospitais. Instituições não podem ser representadas como parceiras ou receber senhas por conta de terceiros sem autorização e verificação institucional.

O administrador registra somente instituições verificadas, aprova explicitamente a parceria e a agenda, vincula profissionais à unidade e entrega a credencial **de forma privada** ao titular. A senha é armazenada como hash scrypt, não pode ser lida no banco, nem publicada em README. Para verificar contas autorizadas, consulte o cadastro administrativo da unidade e sua equipe autenticada; não há senhas reais para listar aqui.

## Cobertura por estado (consulta pública, não convênio)

O site possui acesso ao diretório nacional da Rede Brasileira de Bancos de Leite Humano para as 27 UFs, com link externo claramente identificado. O estado no diretório não prova a existência de uma unidade MãeLink.

| UF | Estado | Hospitais MãeLink autorizados neste pacote | Contas hospitalares reais neste pacote | Fonte de referência |
|---|---|---:|---:|---|
| AC | Acre | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| AL | Alagoas | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| AP | Amapá | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| AM | Amazonas | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| BA | Bahia | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| CE | Ceará | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| DF | Distrito Federal | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| ES | Espírito Santo | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| GO | Goiás | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| MA | Maranhão | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| MT | Mato Grosso | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| MS | Mato Grosso do Sul | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| MG | Minas Gerais | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| PR | Paraná | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| PB | Paraíba | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| PA | Pará | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| PE | Pernambuco | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| PI | Piauí | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| RN | Rio Grande do Norte | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| RS | Rio Grande do Sul | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| RJ | Rio de Janeiro | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| RO | Rondônia | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| RR | Roraima | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| SC | Santa Catarina | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| SE | Sergipe | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| SP | São Paulo | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |
| TO | Tocantins | 0 | 0 | [Rede oficial](https://rblh.fiocruz.br/localizacao-dos-blhs) |

## Identidades de testes automatizados — NÃO são credenciais de hospitais

Os testes em `site/tests/integration.test.js` geram, exclusivamente num banco temporário, as seguintes identidades fictícias: `admin@hospitais.test`, `nurse@hospitais.test`, `manager@hospitais.test`, `mae1@example.test` e `mae2@example.test`. Não existem em uma instalação nova e **não podem ser usadas em hospitais reais**. As senhas de teste estão no código de testes para reprodução local; nenhuma é uma senha institucional.

## Lista pública de São Paulo

O arquivo `site/directory_sp.json` contém 50 referências históricas (fonte e ano no próprio arquivo). A página exibe somente quatro inicialmente e oferece o botão **Ver mais opções**. Nenhuma referência é ativada como hospital conveniado.

## Requisitos pendentes para operação real

Antes de abrir para mães reais, obter autorização formal de cada hospital, dados atualizados, localização geográfica confirmada, horários oficialmente disponibilizados, responsável pelo tratamento de dados, procedimentos de privacidade e validação clínica. O pedido administrativo e a agenda publicados no portal NÃO integram automaticamente as agendas dos sistemas hospitalares.

---

## Procedimento técnico anterior (referência)

# MãeLink — cadastro e credenciamento de hospitais

> **Versão para validação institucional, não homologada para dados reais.** A relação de 50 referências de São Paulo é histórica (Secretaria da Saúde, atualização de 03/05/2016). Nenhuma instituição nessa relação é tratada automaticamente como parceira nem recebe agendamentos sem credenciamento e vagas autorizadas. Verifique todos os dados antes de ativar.

## 1. Como criar a primeira conta ADMIN

A conta administrativa **não tem senha padrão** no projeto. Crie um e-mail institucional e defina uma senha única, forte e secreta no próprio ambiente do servidor. No PowerShell, na pasta `site`:

```powershell
$env:ADMIN_EMAIL='admin@SEU-DOMINIO-INSTITUCIONAL'
$env:ADMIN_PASSWORD='SUBSTITUA_POR_UMA_SENHA_UNICA_FORTE'
node server.js
```

Acesse `http://127.0.0.1:3000/entrar.html` com as credenciais definidas **por você**. A conta é criada somente na primeira inicialização quando não houver ADMIN; nenhuma senha real é gravada neste README. Depois da criação, retire a variável `ADMIN_PASSWORD` dos processos/configurações de inicialização e gerencie segredos em cofre institucional. Uma instalação real exige HTTPS, MFA/SSO para a equipe e revisão de segurança.

**Não use** `admin@hospital.test`, senhas de exemplo ou dados de pacientes reais em ambiente público.

## 2. Como o hospital solicita seu cadastro

1. Abra `/hospital-cadastro.html` pelo site público.
2. Preencha nome institucional, CNPJ, cidade, UF, endereço e contato do responsável institucional; confirme a declaração.
3. O servidor registra a solicitação com um protocolo próprio, inicialmente `PENDENTE`. **Não cria automaticamente login, hospital ativo ou vaga.**
4. Um ADMIN entra em `Meu espaço → Solicitações de cadastro de hospitais` e pode marcar `EM_ANALISE`, `RECUSADA` ou `APROVADA` após conferir documentos por canais independentes. Aprovar o *pedido* não é o mesmo que ativar a unidade.

## 3. Como credenciar a unidade e criar sua conta de equipe

1. Valide que o banco de leite está operante, seu endereço, CNPJ, representação e autorização formal para publicar agenda; documente convênio e responsabilidade pela confirmação.
2. No painel ADMIN, em `Credenciamento de hospitais → Adicionar uma unidade`, informe os dados oficiais verificados. Se tiver **latitude e longitude verificadas**, preencha ambas para colocar o marcador no mapa. Não use o centro da cidade como coordenada de hospital.
3. A unidade entra como `PENDENTE`. Somente após receber autorização de parceria e agenda, o ADMIN pode acionar `Ativar parceria verificada`, declarando as duas confirmações. O sistema não verifica documentos sozinho.
4. Em `Vincular integrante à unidade`, crie um login profissional separado para **cada pessoa autorizada**, com senha temporária única e perfil `ATENDENTE` ou `GESTOR`, vinculado ao **ID da unidade correta**. Transmita a senha por canal institucional seguro e substitua-a em ambiente de identidade com troca obrigatória/MFA antes de operar dados reais.
5. O GESTOR publica as vagas validadas pela unidade. A MÃE solicita um horário; a equipe dessa mesma unidade confirma, cancela ou registra a doação. A mãe vê o histórico apenas em sua conta.

**Uma unidade = um ID próprio.** Cada pedido, vaga, profissional e doação é associado ao ID do hospital no banco interno. A lista histórica de referências **não gera** IDs de unidades credenciadas nem contas automaticamente.

## 4. Diretório de referências de São Paulo

Arquivo: `site/directory_sp.json`, com **50 referências históricas** citadas pela Secretaria de Estado da Saúde de SP em 2016. A listagem contém bancos de leite e serviços, **não são 50 hospitais credenciados**; alguns serviços são centros/BLHs e não hospitais. Ela serve apenas para descoberta e verificação humana, nunca como agenda.

Fonte original: https://www.saude.sp.gov.br/resources/ses/perfil/profissional-da-saude/homepage/grupo-tecnico-de-acoes-estrategicas-gtae/saude-da-crianca/bancos_de_sangue_sp_e_interior.pdf

Diretório da Rede Brasileira de Bancos de Leite Humano para confirmação atual: https://rblh.fiocruz.br/localizacao-dos-blhs

**Importante:** endereço, funcionamento, nome e participação podem ter mudado desde 2016. Nunca publique disponibilidade nem use marca de hospital como parceiro sem autorização atual da instituição.

## 5. Mapa e privacidade

O mapa de São Paulo está embutido no site como um SVG esquemático e **não carrega tiles, Leaflet ou CDNs externas**. Marcadores de hospitais são exibidos apenas quando a instituição estiver credenciada e possuir coordenadas confirmadas. A localização do visitante é opcional e exige permissão do navegador (HTTPS ou localhost); ela é processada no navegador para ordenar unidades credenciadas por distância aproximada e não é enviada à API nem gravada no banco. Nenhum hospital sem coordenadas recebe marcador inventado. O diretório de 27 estados leva à rede oficial, sem assumir parceria.

## 6. Integração hospitalar ainda necessária antes da operação pública

A agenda SQLite é interna e **não conversa com a agenda oficial de nenhum hospital**. Operação real exige contrato/integrador autorizado, sincronização bidirecional ou processo operacional acordado, reconciliar vagas entre canais, logs adequados, notificações, LGPD e revisão por responsáveis clínicos e DPO. O cadastro de hospital não prova vínculo por si só. O pacote não contém credenciais reais nem hospitais habilitados por padrão.
