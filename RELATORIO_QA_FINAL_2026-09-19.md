# MãeLink — Relatório de auditoria técnica da versão enviada

**Data da revisão:** 19/09/2026. **Base:** arquivo fornecido `MaeLink_QA_Localizacao_Assistente_Atualizado_RM554628(1).zip`; alterações preservam estrutura e projeto original.

## Correções realizadas no site

1. **Geolocalização nativa desbloqueada:** cabeçalho HTTP `Permissions-Policy` proibia o recurso (`geolocation=()`); agora permite à própria origem (`geolocation=(self)`). A solicitação continua dependendo de clique do usuário, permissão do navegador e contexto seguro HTTPS/localhost. Não é possível forçar a exibição de nova janela se já houver permissão definida ou política do dispositivo bloquear.
2. **Consentimento e cadastro:** API só aceita `privacy_consent` booleano `true`, rejeitando texto `"false"` e número `1`; datas impossíveis, bebê com nascimento anterior ao da mãe e nomes incompatíveis são recusados; idade mínima verificada pelo aniversário civil no fuso configurado.
3. **Agenda:** data/hora de publicação e reserva são calculadas no fuso `HOSPITAL_TIME_ZONE` (padrão `America/Sao_Paulo`), evitando comparação acidental com UTC/fuso do servidor. **Bloqueio de produção:** implantação em unidades de fusos diferentes ainda requer fuso individual por hospital.
4. **Entrada e HTTP:** montagem de JSON via buffers evita corromper UTF-8 quando um caractere é dividido entre pacotes; URLs percent-encoded inválidas geram 400; requisição HEAD a arquivo estático não envia corpo.
5. **Autorização:** checagem de vínculo hospitalar precede a consulta de histórico institucional; escopo por mãe, equipe e hospital preservado.
6. **Backup:** `site/scripts/backup.mjs` gera cópia SQLite consistente (inclusive WAL), verifica integridade e não sobrescreve arquivo existente. Agendamento, retenção e criptografia de cópias permanecem responsabilidades de operação.

## Como reproduzir os testes

```powershell
cd site
npm test
```

**Resultado final:** 38 testes automatizados aprovados em **cada uma de três rodadas independentes**, 114 execuções aprovadas no total; zero falhas nas três rodadas finais. O conjunto abrange cadastro, login, sessão, CSRF/origem, acesso da mãe e da equipe, credenciamento hospitalar, capacidade e cancelamento de vagas, confirmação e histórico de doação, chatbot sem chave e com OpenAI simulada, geolocalização por cabeçalho, validação de perfis, JSON UTF-8, métodos HTTP, fuso e backup/restauração de cópia. A primeira execução de expansão dos testes expôs um erro **no teste** por atingir o limite intencional de registros; corrigido para testar os dados inválidos via atualização de perfil, sem afrouxar o limite de produção. Todas as três rodadas finais passaram.

**Validação estática adicional:** 9 arquivos HTML analisados (links internos, `id` duplicados, `lang` e título), sem problemas encontrados; verificações de sintaxe executadas em todos os arquivos `.js`/`.mjs` do site. Não foram encontradas chaves OpenAI nos arquivos públicos. Arquivos `.sqlite`, `.env` reais e backups não são incluídos no pacote.

## O que não foi possível validar — NÃO interpretar como aprovado

- **Navegador gráfico:** Chromium foi iniciado, mas o ambiente bloqueou acesso a `127.0.0.1` (`ERR_BLOCKED_BY_ADMINISTRATOR`). Portanto, não se confirmou a janela nativa real de GPS, layout em dispositivos reais, leitores de tela ou uma navegação visual ponta a ponta.
- **OpenAI real:** não foi fornecida chave de API; teste de contrato com resposta externa simulada não prova funcionamento, qualidade clínica, custo nem proteção integral contra vazamento. O chatbot não substitui equipe de saúde.
- **Hospitais reais:** não há unidades conveniadas/validadas nem horários reais pré-carregados. Diretório de 50 referências é histórico e não equivale a rede parceira; agendamento interno não integra prontuários nem sistemas oficiais de agenda.
- **Outras aplicações incluídas:** fontes legadas Java/Spring e Android Kotlin permanecem em `codigo_original` para referência, **não são publicadas pelo site**; não houve compilação de Maven/Gradle/Flutter nesta revisão. A API legada está expressamente identificada como `nao_publicar`.
- **Operação pública:** pendem avaliação independente de segurança, teste de carga, homologação clínica, contratos hospitalares, governança LGPD/dados infantis, MFA para equipes, recuperação de senha, TLS/HTTPS publicado, criptografia em repouso, backups automáticos cifrados, restauração operacional e acompanhamento de incidentes. Não existem garantias de “100% sem erros” ou de nota acadêmica.

## Fontes técnicas verificadas

- OWASP Authorization Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html
- OpenStreetMap Tile Usage Policy: https://operations.osmfoundation.org/policies/tiles/
- Node.js SQLite backup API: https://nodejs.org/download/release/v22.16.0/docs/api/sqlite.html
- OpenAI API Key Safety: https://help.openai.com/en/articles/5112595-best-practices-for-api-key-safety
- Autoridade Nacional de Proteção de Dados: https://www.gov.br/anpd/pt-br

**Status:** artefato acadêmico auditado para validação local; **não homologado para uso público com pacientes reais**.
