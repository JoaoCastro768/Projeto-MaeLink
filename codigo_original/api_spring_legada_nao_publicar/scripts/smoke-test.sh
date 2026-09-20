#!/usr/bin/env bash
set -euo pipefail
BASE_URL="${BASE_URL:-http://localhost:8080}"

echo "[1/8] Health"
curl -fsS "$BASE_URL/actuator/health"; echo

echo "[2/8] Login JWT"
AUTH_JSON=$(curl -fsS -X POST "$BASE_URL/api/v1/auth/login" \
  -H 'Content-Type: application/json' \
  -d '{"identificador":"ana.martins@example.com","senha":"demo123"}')
TOKEN=$(printf '%s' "$AUTH_JSON" | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')
[ -n "$TOKEN" ] || { echo "Login não retornou accessToken" >&2; exit 1; }

echo "[3/8] Validar JWT"
curl -fsS "$BASE_URL/api/v1/auth/validate" -H "Authorization: Bearer $TOKEN"; echo

echo "[4/8] Bancos"
curl -fsS "$BASE_URL/api/v1/bancos"; echo

echo "[5/8] Doadora seed"
curl -fsS "$BASE_URL/api/v1/doadoras/1"; echo

echo "[6/8] Resultado triagem seed"
curl -fsS "$BASE_URL/api/v1/triagens/1/resultado"; echo

echo "[7/8] Timeline doação seed"
curl -fsS "$BASE_URL/api/v1/doacoes/1/timeline"; echo

echo "[8/8] Auditoria"
curl -fsS "$BASE_URL/api/v1/auditorias?page=0&size=5"; echo

echo "Smoke test concluído."
