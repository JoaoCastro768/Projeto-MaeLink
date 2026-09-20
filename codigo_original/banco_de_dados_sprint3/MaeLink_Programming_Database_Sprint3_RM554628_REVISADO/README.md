# MãeLink - Programming and Database Management - Sprint 3

**Integrante:** João Castro - RM 554628  
**Projeto:** MãeLink  
**Banco alvo:** PostgreSQL 16  
**Repositório:** https://github.com/JoaoCastro768/maelink-programming-database-sprint3

## Objetivo

Implementar a camada de dados do MãeLink a partir da arquitetura definida na Sprint 2, cobrindo cadastro de doadora, triagem, bancos de leite, agendamento, doação, rastreabilidade por lote, usuários, notificações e auditoria.

## Cobertura da rubrica

| Critério | Entrega |
|---|---|
| Modelo lógico revisado | `docs/MaeLink_Modelo_Logico_Revisado_RM554628.pdf` |
| DDL com PK, FK, UNIQUE, CHECK e NOT NULL | `sql/01_ddl.sql` |
| Massa mínima de 100 registros | `sql/02_massa_dados.sql` - **221 registros** |
| Evidência de implementação | `evidencias/GUIA_EVIDENCIAS.md` + scripts de evidência |
| 5 consultas operacionais | `sql/03_consultas_operacionais.sql` |
| 5 consultas relacionais | `sql/04_consultas_relacionais.sql` |
| 5 consultas analíticas | `sql/05_consultas_analiticas.sql` |
| 5 indicadores executivos | `sql/06_dashboard_indicadores.sql` + PDF do dashboard |

## Estrutura

```text
sql/
  00_execucao_completa.sql
  01_ddl.sql
  02_massa_dados.sql
  03_consultas_operacionais.sql
  04_consultas_relacionais.sql
  05_consultas_analiticas.sql
  06_dashboard_indicadores.sql
  07_validacoes_integridade.sql
docs/
  MaeLink_Modelo_Logico_Revisado_RM554628.pdf
  MaeLink_Dashboard_Executivo_RM554628.pdf
  assets/
resultados/
  contagem_registros.csv
  operacionais_*.csv
  relacionais_*.csv
  analiticas_*.csv
  indicador_*.csv
evidencias/
  GUIA_EVIDENCIAS.md
  validacao_local.txt
  RELATORIO_VALIDACAO_AUTOMATICA.md
scripts/
  executar_evidencias.ps1
  testar_entrega_limpa.ps1
  validar_local.py
docker-compose.yml
README.md
```

## Modelo lógico revisado

O modelo contém **9 entidades**: `DOADORA`, `TRIAGEM`, `BANCO_LEITE`, `AGENDAMENTO`, `DOACAO`, `LOTE_LEITE`, `USUARIO`, `NOTIFICACAO` e `AUDITORIA`. As cardinalidades foram descritas considerando a opcionalidade real das FKs; por exemplo, uma doadora pode existir antes de possuir triagens/agendamentos, e um agendamento pode gerar zero ou uma doação.

## Massa de dados

A carga contém **221 registros fictícios**, distribuídos assim:

| Tabela | Registros |
|---|---:|
| `usuario` | 15 |
| `doadora` | 30 |
| `banco_leite` | 10 |
| `triagem` | 30 |
| `agendamento` | 28 |
| `doacao` | 24 |
| `lote_leite` | 24 |
| `notificacao` | 30 |
| `auditoria` | 30 |

Os dados são fictícios. CPF e senha são representados somente por hashes fictícios.

## Consultas e inteligência de negócio

- 5 consultas operacionais com `SELECT`, `WHERE`, `ORDER BY` e `DISTINCT`;
- 5 consultas relacionais usando `INNER JOIN`, `LEFT JOIN` e `RIGHT JOIN`;
- 5 consultas analíticas com `GROUP BY`, `HAVING`, funções de agregação e subqueries;
- 5 consultas próprias para o dashboard executivo.

Indicadores da massa atual:

1. Volume válido registrado: **11.978 ml (11,98 L)**;
2. Taxa de triagens aptas: **73,33%**;
3. Conversão de doadoras aptas em agendamento: **81,82%**;
4. Banco com maior volume válido: **Banco de Leite Guarulhos - 1.802 ml**;
5. Doadoras aptas sem agendamento: **4**.

## Executar com Docker (recomendado)

Pré-requisito: Docker Desktop em execução.

```powershell
docker compose up -d
```

O PostgreSQL ficará disponível em `localhost:5433`:

```text
Database: maelink_db
User: maelink
Password: maelink123
```

Os scripts `01_ddl.sql` e `02_massa_dados.sql` são executados automaticamente na **primeira criação do volume**.

Para abrir o psql:

```powershell
docker exec -it maelink-postgres psql -U maelink -d maelink_db
```

### Executar os arquivos de consulta diretamente no PostgreSQL

```powershell
docker exec maelink-postgres psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -f /maelink/sql/03_consultas_operacionais.sql
docker exec maelink-postgres psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -f /maelink/sql/04_consultas_relacionais.sql
docker exec maelink-postgres psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -f /maelink/sql/05_consultas_analiticas.sql
docker exec maelink-postgres psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -f /maelink/sql/06_dashboard_indicadores.sql
```

## Gerar evidências automaticamente

```powershell
.\scripts\executar_evidencias.ps1
```

O script executa contagem, validações de integridade, consultas operacionais, relacionais, analíticas e os cinco indicadores.

## Teste completo em ambiente limpo

```powershell
.\scripts\testar_entrega_limpa.ps1
```

Esse teste remove o volume Docker **deste projeto**, recria o banco do zero e falha automaticamente se a carga ou alguma consulta não executar.

## Validação local sem Docker

Há também um teste auxiliar usando a biblioteca SQLite do próprio Python:

```powershell
python .\scripts\validar_local.py
```

Ele verifica as 9 tabelas, os 221 registros, as 20 consultas principais, integridade referencial, constraints negativas e os valores dos cinco KPIs. O banco-alvo da entrega continua sendo **PostgreSQL 16**.

## Encerrar

```powershell
docker compose down
```

Para apagar o volume e permitir que os scripts de inicialização sejam executados novamente:

```powershell
docker compose down -v
docker compose up -d
```
