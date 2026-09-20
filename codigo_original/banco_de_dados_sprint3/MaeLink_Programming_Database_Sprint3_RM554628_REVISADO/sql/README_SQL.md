# Ordem dos scripts SQL

1. `01_ddl.sql` - cria as 9 tabelas, constraints e índices.
2. `02_massa_dados.sql` - insere 221 registros fictícios.
3. `03_consultas_operacionais.sql` - 5 consultas operacionais.
4. `04_consultas_relacionais.sql` - 5 consultas com INNER/LEFT/RIGHT JOIN.
5. `05_consultas_analiticas.sql` - 5 consultas analíticas.
6. `06_dashboard_indicadores.sql` - 5 KPIs do dashboard executivo.
7. `07_validacoes_integridade.sql` - validações extras de consistência; resultado esperado = 0 erros.

`00_execucao_completa.sql` usa `\ir`, portanto os caminhos são resolvidos relativamente ao próprio arquivo no psql.
