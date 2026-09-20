# MãeLink — portal hospitalar, versão com jornada de cadastro e credenciamento

**Status: versão de desenvolvimento, para homologação técnica e institucional. Não publique dados de pacientes nem anuncie vagas de hospitais sem parceria, consentimentos e infraestrutura aprovados.**

## Executar localmente no Windows

Instale Node.js 22.16 ou superior. Extraia o ZIP, abra a pasta `MaeLink_Entrega_Final_2026_RM554628` e execute no PowerShell:

```powershell
cd site
node server.js
```

Abra `http://127.0.0.1:3000` (o servidor escuta somente no computador local). Para configurar uma conta administrativa, siga **`README_CADASTRO_HOSPITAIS.md`**; nenhuma credencial real é fornecida no pacote. Para testes, rode `npm test` dentro de `site`. Para backup consistente do SQLite: `npm run backup -- /CAMINHO_SEGURO/backup.sqlite` (não use pasta pública; proteja/cripte a cópia).

## O que foi implementado

- **Formulário da mãe:** mensagens de validação por campo conforme a digitação, mudança de campo ou saída; e-mail, senhas, telefone, datas e nome. Validação obrigatória também no servidor. Tela de cadastro concluído com botão de login.
- **Cabeçalho:** botões Entrar e Cadastrar lado a lado, inclusive em telas menores.
- **Conta da mãe:** pedidos, situação, histórico e acesso separado à página **Configurações**, onde pode atualizar os dados do bebê e da mãe.
- **Pedido de horário:** apenas em vagas cadastradas por gestores de unidade efetivamente credenciada. Depois da solicitação, tela de próximos passos e campo para perguntar à assistente. O pedido só é confirmado após ação de profissional habilitado.
- **Hospitais:** formulário público de solicitação de credenciamento, fila no ADMIN, cadastro institucional separado, vínculo de profissionais à unidade, agenda e doações associadas ao hospital correto.
- **São Paulo:** diretório de 50 referências da SES-SP de 2016 **sem agendamento**; listagem de parceiros ativados separada. Mapa regional esquemático próprio em SVG (sem mapa viário ou tiles externos), com marcadores apenas para parceiros que possuem coordenadas confirmadas. O GPS usa a autorização nativa do navegador após clicar no botão, desde que a página esteja em HTTPS ou localhost.
- **Assistente:** botão flutuante inferior direito em páginas principais; exibe a pergunta escrita pela pessoa, não “pergunta recebida”. A API OpenAI é opcional e só funciona com `OPENAI_API_KEY` no servidor; filtros e limites reduzem, mas não eliminam erros ou risco de envio voluntário de dados sensíveis.
- **Visual:** azul-bebê, rosa-claro, gota branca, layout responsivo e elementos acessíveis.

## Páginas principais

`/`, `/cadastro.html`, `/cadastro-concluido.html`, `/entrar.html`, `/minha-conta.html`, `/configuracoes.html`, `/hospital-cadastro.html`, `/solicitacao-concluida.html` e `/assistente.html`.

## Atenção para uso com hospitais reais

Não foi estabelecido nenhum convênio nem realizada integração com agendas externas. A relação de referências públicas é antiga e não indica hospitais parceiros. O administrador deve validar e ativar cada unidade de modo independente. A confirmação continua sendo responsabilidade da equipe do hospital.

Leia `documentacao/SEGURANCA_E_IMPLANTACAO.md` e `README_CADASTRO_HOSPITAIS.md` antes de publicar. São indispensáveis contratos, base legal LGPD, política de privacidade/retenção, HTTPS, MFA da equipe, criptografia em repouso, backups/restauração, notificações e auditoria de segurança e clínica. Não há garantia de que uma IA responda corretamente a todas as dúvidas.

**As imagens de apresentação de versões anteriores foram retiradas do pacote principal**, pois a inspeção visual automatizada em navegador local não pôde ser concluída neste ambiente. Faça revisão visual real no seu Windows antes de publicar.
