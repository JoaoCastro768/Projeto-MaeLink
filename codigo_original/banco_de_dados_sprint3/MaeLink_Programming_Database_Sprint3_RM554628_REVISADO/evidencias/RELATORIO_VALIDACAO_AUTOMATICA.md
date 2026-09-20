# Relatório de validação automática - MãeLink Sprint 3

**Projeto:** MãeLink  
**Disciplina:** Programming and Database Management  
**Integrante:** João Castro - RM 554628

## Resultado da revisão técnica

- Validação local SQL: **APROVADA**.
- Banco auxiliar de teste: SQLite 3.46.1.
- Banco alvo da entrega: **PostgreSQL 16**.
- Total carregado: **221 registros**.
- Tabelas criadas: **9**.
- Consultas obrigatórias executadas: **15/15**.
- Consultas do dashboard executadas: **5/5**.
- Testes negativos de constraints: **4/4 rejeitados corretamente**.
- Arquivos de texto com caracteres de controle inválidos: **0**.
- PDFs: abrem corretamente e passaram no preflight básico.

## Estrutura DDL verificada

- 9 `CREATE TABLE`;
- 9 chaves primárias;
- 9 chaves estrangeiras;
- constraints `UNIQUE`;
- constraints `CHECK`;
- campos `NOT NULL`;
- índices auxiliares para triagem, agendamento, doação, lote, notificação e auditoria.

## Testes de integridade

Foram verificados:

1. FK inválida em triagem - rejeitada;
2. e-mail duplicado de usuário - rejeitado;
3. volume negativo de doação - rejeitado;
4. estoque acima da capacidade do banco - rejeitado;
5. ausência de registros órfãos nas relações principais;
6. consistência entre doação e seu agendamento;
7. limites de volume e capacidade.

## Indicadores confirmados

| Indicador | Resultado |
|---|---:|
| Volume total válido | 11.978 ml (11,98 L) |
| Taxa de triagens aptas | 73,33% |
| Conversão apta -> agendamento | 81,82% |
| Maior volume válido | Banco de Leite Guarulhos - 1.802 ml |
| Doadoras aptas sem agendamento | 4 |

## Compatibilidade PostgreSQL revisada

Foi corrigido um ponto crítico da versão anterior: valores de colunas `BOOLEAN` na massa agora usam `TRUE/FALSE`, compatíveis com PostgreSQL, em vez de `1/0`. Também foram corrigidos caminhos do README e o script completo passou a usar `\ir`, evitando dependência do diretório atual no `psql`.

## Limitação desta validação

O ambiente usado para esta revisão não possui Docker nem `psql`, portanto **não foi possível iniciar uma instância real do PostgreSQL 16 aqui**. Para eliminar esse último risco antes da entrega, execute no Windows, com Docker Desktop aberto:

```powershell
.\scripts\testar_entrega_limpa.ps1
```

O script recria o volume do projeto, carrega os arquivos do zero e executa automaticamente todas as consultas obrigatórias, indicadores e validações de integridade.
