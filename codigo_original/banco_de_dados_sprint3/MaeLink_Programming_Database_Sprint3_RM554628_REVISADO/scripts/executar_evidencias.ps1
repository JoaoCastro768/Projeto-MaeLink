$ErrorActionPreference = "Stop"
$container = "maelink-postgres"
$psql = "psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db"

Write-Host "[1/7] Subindo PostgreSQL..."
docker compose up -d

Write-Host "[2/7] Aguardando banco ficar pronto..."
$ready = $false
for ($i = 0; $i -lt 30; $i++) {
    docker exec $container pg_isready -U maelink -d maelink_db *> $null
    if ($LASTEXITCODE -eq 0) { $ready = $true; break }
    Start-Sleep -Seconds 2
}
if (-not $ready) { throw "PostgreSQL nao ficou pronto dentro do tempo esperado." }

New-Item -ItemType Directory -Force -Path ".\evidencias" | Out-Null

Write-Host "[3/7] Contagem de registros..."
$contagem = docker exec $container psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -P pager=off -c "SELECT 'usuario' tabela, COUNT(*) registros FROM usuario UNION ALL SELECT 'doadora',COUNT(*) FROM doadora UNION ALL SELECT 'banco_leite',COUNT(*) FROM banco_leite UNION ALL SELECT 'triagem',COUNT(*) FROM triagem UNION ALL SELECT 'agendamento',COUNT(*) FROM agendamento UNION ALL SELECT 'doacao',COUNT(*) FROM doacao UNION ALL SELECT 'lote_leite',COUNT(*) FROM lote_leite UNION ALL SELECT 'notificacao',COUNT(*) FROM notificacao UNION ALL SELECT 'auditoria',COUNT(*) FROM auditoria;"
$contagem | Tee-Object -FilePath ".\evidencias\01_contagem_postgresql.txt"

Write-Host "[4/7] Validacoes de integridade..."
docker exec $container psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -P pager=off -f /maelink/sql/07_validacoes_integridade.sql | Tee-Object -FilePath ".\evidencias\02_integridade_postgresql.txt"

Write-Host "[5/7] Consultas operacionais e relacionais..."
docker exec $container psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -P pager=off -f /maelink/sql/03_consultas_operacionais.sql | Tee-Object -FilePath ".\evidencias\03_operacionais_postgresql.txt"
docker exec $container psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -P pager=off -f /maelink/sql/04_consultas_relacionais.sql | Tee-Object -FilePath ".\evidencias\04_relacionais_postgresql.txt"

Write-Host "[6/7] Consultas analiticas e dashboard..."
docker exec $container psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -P pager=off -f /maelink/sql/05_consultas_analiticas.sql | Tee-Object -FilePath ".\evidencias\05_analiticas_postgresql.txt"
docker exec $container psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -P pager=off -f /maelink/sql/06_dashboard_indicadores.sql | Tee-Object -FilePath ".\evidencias\06_dashboard_postgresql.txt"

Write-Host "[7/7] Concluido. Os resultados foram salvos em .\evidencias\."
Write-Host "Agora tire prints do terminal/cliente SQL conforme GUIA_EVIDENCIAS.md."
