# MãeLink — revisão desta atualização

## O que foi implementado
- Cadastro da doadora com validação de campos durante digitação, mudança e saída do campo, e revalidação no servidor.
- Botões Entrar e Cadastrar lado a lado no cabeçalho desktop e mobile.
- Tela de cadastro concluído com botão para entrar; edição do perfil e do bebê em Configurações.
- Diretório de 50 referências históricas do Estado de São Paulo com fonte e data, **sem tratar instituições como parceiras ou divulgar vagas**.
- Cada hospital efetivamente credenciado recebe ID próprio; agenda, equipe, solicitação e histórico ficam vinculados ao hospital correto.
- Formulário público de candidatura de hospital com protocolo e fila de análise pelo ADMIN; candidatura aprovada não equivale a credenciamento ativo.
- Mapa de São Paulo opt-in, com marcadores exclusivamente de parceiros com coordenadas verificadas fornecidas pelo ADMIN.
- Assistente flutuante no canto inferior direito: exibe literalmente a pergunta digitada pela usuária. A chamada OpenAI exige consentimento e chave exclusiva no servidor; limites e bloqueios de conteúdo pessoal/clínico seguem documentação de segurança.
- Após solicitar vaga: página de próximos passos e conversa com a assistente.

## Testes executados
- `node --check` nos 3 arquivos JS de aplicação: sem erros de sintaxe.
- `npm test` três vezes após a última atualização CSS: 27 testes aprovados em cada rodada, total 81 aprovações e zero falhas.
- Inspeção visual em Chromium por renderização **offline do HTML/CSS/JS real** em páginas Início, Cadastro, Entrar (390 e 320 px) e Solicitação institucional: sem erros JS detectados, sem overflow horizontal, ambas ações de autenticação visíveis no mobile.
- Validação durante interação verificou mensagens de nome incompleto e de senhas divergentes.
- A navegação do Chromium até localhost não foi possível por restrição de rede do ambiente; os testes de rotas/API foram feitos por HTTP automatizado no servidor Node. Os testes da OpenAI usam mock externo, não credencial real.

> **Histórico:** os testes citados acima dizem respeito à revisão anterior. Para a versão atualmente entregue, consulte `../RELATORIO_QA_NOVA_VERSAO.md` e os resultados de 30 testes em três rodadas.

## Limites de implantação
- Diretório histórico SP: Secretaria de Estado da Saúde, documento atualizado em 03/05/2016. Verificação de nome, contato, credenciamento, localização e autorização atual é indispensável.
- **Não há integração com agendas externas de hospitais**. A agenda funciona apenas na base interna e não confirma consultas até o profissional do hospital aprovar.
- Atualização posterior deste pacote: mapa regional SVG **sem** Leaflet/tiles externos; só mostra coordenadas reais cadastradas para unidades ativas. Pode ficar sem marcadores. Consulte `../RELATORIO_QA_NOVA_VERSAO.md` para alterações e testes atuais.
- **Não utilizar dados de pacientes em produção** antes de revisão clínica, jurídica/LGPD, testes de segurança independentes, HTTPS, MFA para equipe e convênios institucionais.
- Não é possível garantir que respostas de IA não tenham erros; perguntas clínicas devem ser encaminhadas a profissionais habilitados.

Fonte histórica do diretório: https://www.saude.sp.gov.br/resources/ses/perfil/profissional-da-saude/homepage/grupo-tecnico-de-acoes-estrategicas-gtae/saude-da-crianca/bancos_de_sangue_sp_e_interior.pdf
