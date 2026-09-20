$BaseUrl = if ($env:BASE_URL) { $env:BASE_URL } else { "http://localhost:8080" }

Write-Host "[1/8] Health"
Invoke-RestMethod "$BaseUrl/actuator/health" | ConvertTo-Json -Depth 10

Write-Host "[2/8] Login JWT"
$LoginBody = @{ identificador = "ana.martins@example.com"; senha = "demo123" } | ConvertTo-Json
$Auth = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/v1/auth/login" -ContentType "application/json" -Body $LoginBody
$Token = $Auth.accessToken
if (-not $Token) { throw "Login não retornou accessToken" }

Write-Host "[3/8] Validar JWT"
Invoke-RestMethod -Uri "$BaseUrl/api/v1/auth/validate" -Headers @{ Authorization = "Bearer $Token" } | ConvertTo-Json -Depth 10

Write-Host "[4/8] Bancos"
Invoke-RestMethod "$BaseUrl/api/v1/bancos" | ConvertTo-Json -Depth 10

Write-Host "[5/8] Doadora seed"
Invoke-RestMethod "$BaseUrl/api/v1/doadoras/1" | ConvertTo-Json -Depth 10

Write-Host "[6/8] Resultado triagem seed"
Invoke-RestMethod "$BaseUrl/api/v1/triagens/1/resultado" | ConvertTo-Json -Depth 10

Write-Host "[7/8] Timeline doação seed"
Invoke-RestMethod "$BaseUrl/api/v1/doacoes/1/timeline" | ConvertTo-Json -Depth 10

Write-Host "[8/8] Auditoria"
Invoke-RestMethod "$BaseUrl/api/v1/auditorias?page=0&size=5" | ConvertTo-Json -Depth 10

Write-Host "Smoke test concluído."
