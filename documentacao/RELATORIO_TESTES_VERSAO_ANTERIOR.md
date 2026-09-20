# Relatório de testes — 18/09/2026

Ambiente da execução: Node.js v22.16.0, `node:test`, backend HTTP local e banco SQLite temporário independente por rodada.

| Rodada | Testes automáticos | Resultado |
| --- | ---: | --- |
| 1 | 10 | 10 passaram; 0 falharam |
| 2 | 10 | 10 passaram; 0 falharam |
| 3 | 10 | 10 passaram; 0 falharam |

Detalhes: `teste_rodada_1.log`, `teste_rodada_2.log` e `teste_rodada_3.log`.

Casos cobertos: saúde e arquivos estáticos, cabeçalhos de segurança, bases fictícias e fontes oficiais, validação de cadastro e consentimento, senha correta/incorreta, separação de dados entre duas doadoras, impossibilidade de promover conta por parâmetro, provisão administrativa, níveis de equipe, transições de status, cancelamento, CSRF, logout, datas inválidas, hash de senha, sanitização de texto e restrição de respostas do assistente a conteúdo predefinido.

**Limites dos testes:** o Chromium automatizado neste ambiente bloqueou a navegação tanto por localhost quanto por arquivo local com `ERR_BLOCKED_BY_ADMINISTRATOR`. Por isso, NÃO houve teste visual de navegador ou verificação pixel a pixel, embora o CSS tenha sido analisado sintaticamente (190 regras) e HTML/links de ativos tenham sido verificados de forma estática. O SDK Flutter e o Maven não estavam disponíveis, então não foi possível executar ou retestar os projetos legados Flutter/Java. Os testes não equivalem a pentest nem a validação por usuários finais. Um teste manual no computador do usuário continua obrigatório antes de publicação ou apresentação.
