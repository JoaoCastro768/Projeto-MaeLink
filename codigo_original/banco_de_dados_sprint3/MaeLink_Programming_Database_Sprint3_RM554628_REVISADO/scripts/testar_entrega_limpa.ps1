$ErrorActionPreference = "Stop"
Write-Host "ATENCAO: este teste recria o volume Docker do projeto MãeLink." -ForegroundColor Yellow
Write-Host "[1/5] Removendo ambiente anterior..."
docker compose down -v --remove-orphans
Write-Host "[2/5] Construindo banco limpo a partir de 01_ddl.sql e 02_massa_dados.sql..."
docker compose up -d
$ready = $false
for ($i = 0; $i -lt 30; $i++) {
    docker exec maelink-postgres pg_isready -U maelink -d maelink_db *> $null
    if ($LASTEXITCODE -eq 0) { $ready = $true; break }
    Start-Sleep -Seconds 2
}
if (-not $ready) { docker compose logs postgres; throw "Banco nao iniciou corretamente." }
Write-Host "[3/5] Executando as 15 consultas obrigatorias..."
foreach ($arquivo in @("03_consultas_operacionais.sql", "04_consultas_relacionais.sql", "05_consultas_analiticas.sql")) {
    docker exec maelink-postgres psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -P pager=off -f "/maelink/sql/$arquivo" *> $null
    if ($LASTEXITCODE -ne 0) { throw "Falha ao executar $arquivo" }
}
Write-Host "[4/5] Validando indicadores e integridade..."
docker exec maelink-postgres psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -P pager=off -f /maelink/sql/06_dashboard_indicadores.sql *> $null
if ($LASTEXITCODE -ne 0) { throw "Falha nos indicadores." }
docker exec maelink-postgres psql -v ON_ERROR_STOP=1 -U maelink -d maelink_db -P pager=off -f /maelink/sql/07_validacoes_integridade.sql *> $null
if ($LASTEXITCODE -ne 0) { throw "Falha nas validacoes de integridade." }
$total = docker exec maelink-postgres psql -U maelink -d maelink_db -tAc "SELECT (SELECT COUNT(*) FROM usuario)+(SELECT COUNT(*) FROM doadora)+(SELECT COUNT(*) FROM banco_leite)+(SELECT COUNT(*) FROM triagem)+(SELECT COUNT(*) FROM agendamento)+(SELECT COUNT(*) FROM doacao)+(SELECT COUNT(*) FROM lote_leite)+(SELECT COUNT(*) FROM notificacao)+(SELECT COUNT(*) FROM auditoria);"
if (($total.Trim()) -ne "221") { throw "Total inesperado de registros: $total (esperado 221)." }
Write-Host "[5/5] SUCESSO: banco criado do zero, 221 registros carregados e consultas executadas." -ForegroundColor Green
