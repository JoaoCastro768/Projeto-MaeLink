# Relatório de QA — revisão da navegação e assistente

## Mudanças efetuadas

- Mapa de SP próprio SVG: não solicita imagens de tile de provedores externos; o erro HTTP 403 de mosaicos não é exibido. O desenho é esquemático, sem ruas.
- Botão de geolocalização opcional, com autorização explícita do navegador e feedback para negação, demora, falha e ausência de hospitais. Coordenadas processadas somente no navegador, sem remessa à API. Distâncias aproximadas em linha reta de **parceiros credenciados com coordenadas verificadas**.
- Referências históricas de SP: quatro cartões iniciais, botão "Ver mais opções", busca; removido seletor de quantidade.
- Botões harmonizados no cabeçalho, cartões e formulários; layout responsivo.
- Assistente: quando a chave da OpenAI está ausente, responde questões gerais com textos locais limitados e identificados como automáticos, sem fingir resposta de GPT; a pergunta digitada permanece visível. Com chave configurada, integra API OpenAI conforme autorização e moderação; requisições reais à OpenAI não foram testadas sem chave válida.
- Diretório por 27 unidades federativas aponta para a rede oficial. **Não cria hospitais, senhas, agendas nem marcadores falsos.**

## Precaução de publicação

Não usar dados pessoais reais nem representar disponibilidade hospitalar antes de convênio e validações técnicas/legais. Para integração profissional com mapas de ruas, utilizar provedor contratado ou hospedagem própria com licença e garantias adequadas.

## Testes executados

- **Três rodadas de testes automatizados da API:** 30/30 aprovados em cada rodada, total de 90 execuções aprovadas, nenhuma falha na versão final.
- **Validação estática:** 9 páginas HTML com IDs sem duplicação, links internos existentes, imagens com alt; 27 links estaduais, 50 referências SP, mapa sem CDNs nem tiles externos.
- **Verificação de sintaxe JavaScript:** `server.js`, `care.js`, `portal.js` sem erros de sintaxe.
- **Inspeção visual e interações no navegador:** tentativa efetuada, mas o Chromium do ambiente bloqueia navegação a `127.0.0.1` com `ERR_BLOCKED_BY_ADMINISTRATOR`. Assim, os comportamentos de geolocalização, exibição no navegador e alinhamento em telas reais ainda exigem validação manual; não estão cobertos pelos testes acima.
- **OpenAI:** testes de integração usam mock. Não foi efetuada chamada real sem uma chave válida do titular.

Os testes automatizados não equivalem à homologação com hospitais, revisão clínica ou auditoria independente de segurança.
