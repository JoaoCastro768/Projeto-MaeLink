-- Execução completa via psql.
-- \ir resolve os arquivos relativamente à localização deste script.
\set ON_ERROR_STOP on
\ir 01_ddl.sql
\ir 02_massa_dados.sql
\ir 03_consultas_operacionais.sql
\ir 04_consultas_relacionais.sql
\ir 05_consultas_analiticas.sql
\ir 06_dashboard_indicadores.sql
\ir 07_validacoes_integridade.sql
