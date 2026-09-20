# Guia de evidências - Sprint 3

Os prints finais devem ser feitos com **PostgreSQL 16 realmente em execução**. A validação SQLite incluída é apenas um teste técnico auxiliar.

## Forma mais rápida

Na raiz do projeto, com Docker Desktop aberto:

```powershell
.\scripts\executar_evidencias.ps1
```

O script aguarda o banco ficar pronto, executa consultas e grava saídas em `evidencias/`.

## Prints mínimos recomendados

1. **Tabelas criadas** - no psql execute `\dt` e mostre as 9 tabelas.
2. **Constraints** - execute `\d doadora`, `\d agendamento` e `\d doacao`.
3. **Massa de dados** - mostre `evidencias/01_contagem_postgresql.txt` ou a consulta de contagem; total esperado: **221**.
4. **Integridade** - execute `sql/07_validacoes_integridade.sql`; todas as linhas devem mostrar **0** erros.
5. **Consulta operacional** - capture pelo menos uma consulta de `03_consultas_operacionais.sql`.
6. **Consulta relacional** - capture uma consulta de `04_consultas_relacionais.sql`, de preferência a QR4 com `RIGHT JOIN`.
7. **Consulta analítica** - capture uma consulta com `GROUP BY`, `HAVING` e subquery de `05_consultas_analiticas.sql`.
8. **Dashboard** - execute `06_dashboard_indicadores.sql` e compare com `docs/MaeLink_Dashboard_Executivo_RM554628.pdf`.

## Teste limpo da entrega

Para validar exatamente como o professor faria em um ambiente novo:

```powershell
.\scripts\testar_entrega_limpa.ps1
```

Esse comando apaga **somente o volume Docker deste projeto**, recria o PostgreSQL e verifica carga + consultas.
