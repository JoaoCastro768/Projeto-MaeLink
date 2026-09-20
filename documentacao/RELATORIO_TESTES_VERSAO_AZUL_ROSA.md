# Relatório de testes — MãeLink azul céu / rosa suave

**Data:** 18/09/2026  
**Natureza da solução:** protótipo acadêmico. Nenhum agendamento hospitalar real é realizado.

## Revisões implementadas

- Paleta azul céu, rosa suave, superfícies brancas e tipografia com contraste; CSS responsivo, fundos vetoriais leves e logotipo atualizado.
- Páginas próprias de login (`/entrar.html`) e cadastro (`/cadastro.html`) para mães doadoras; autenticação com sessão `HttpOnly` e perfil DOADORA no servidor.
- Catálogo com 27 polos **fictícios**, um para cada UF, busca por cidade/UF e filtro por estado; cartões paginados de 9 em 9. Nenhum polo representa hospital real.
- Fluxo demonstrativo: escolher polo, autenticar, escolher data, consultar horários fictícios, registrar solicitação e acompanhar status. A API informa horário ocupado e rejeita nova reserva ilustrativa no mesmo slot.
- Administrador cria contas de ATENDENTE e GESTOR; o controle das ações ocorre no servidor, não apenas na interface.

## Evidências

- **Rodada 1:** 13 testes passaram; 0 falhas (`testes_azul_rosa/rodada_1.log`).
- **Rodada 2:** 13 testes passaram; 0 falhas (`testes_azul_rosa/rodada_2.log`).
- **Rodada 3:** 13 testes passaram; 0 falhas (`testes_azul_rosa/rodada_3.log`).
- **Total das três rodadas:** 39 execuções bem-sucedidas.
- Renderização HTML/CSS em Chromium com respostas de API simuladas: capturas desktop/mobile em `../previews/`. A homepage exibe 9 de 27 polos inicialmente; filtro RJ reduz para 1. Em viewport móvel de 390 px, não houve rolagem horizontal no início nem no login.

## Limites e próximos passos para uso real

Este pacote **não** confirma visitas, doações, endereços ou vagas em hospitais. Para operar de verdade, obter autorização da rede de bancos de leite, cadastrar unidades oficiais verificadas, integrar agendas, implementar gestão de privacidade e proteção de dados de saúde, revisão clínica, testes de acessibilidade com pessoas, testes de segurança independentes e deploy HTTPS. Testes locais não demonstram ausência de todos os defeitos.

A inspeção visual usou HTML isolado por restrição administrativa a navegação HTTP do Chromium no ambiente de teste. Os testes automatizados da API utilizaram um servidor HTTP local real. As imagens de `previews/` são prévias, não evidência de agendamento hospitalar verdadeiro.

## Referências consultadas

- Ministério da Saúde — Doação de leite humano: https://www.gov.br/saude/pt-br/assuntos/saude-de-a-a-z/d/doacao-de-leite
- Ministério da Saúde — Como doar: https://www.gov.br/saude/pt-br/assuntos/saude-de-a-a-z/d/doacao-de-leite/como-doar
- ANPD — Guia de Segurança para agentes de tratamento de pequeno porte: https://www.gov.br/anpd/pt-br/assuntos/noticias/anpd-publica-guia-de-seguranca-para-agentes-de-tratamento-de-pequeno-porte
- W3C — WCAG 2.2: https://www.w3.org/TR/WCAG22/
