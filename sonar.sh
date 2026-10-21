#!/usr/bin/env bash
# Analisa os dois projetos no SonarQube local (suba antes com: docker compose up -d).
# Uso: ./sonar.sh            (usa admin/admin na primeira vez)
#      SONAR_USER=admin SONAR_PASSWORD=outra ./sonar.sh
set -euo pipefail

SONAR_URL="${SONAR_URL:-http://localhost:9000}"
SONAR_USER="${SONAR_USER:-admin}"
SONAR_PASSWORD="${SONAR_PASSWORD:-admin}"
cd "$(dirname "$0")"

echo "Aguardando o SonarQube em $SONAR_URL ..."
until curl -fs "$SONAR_URL/api/system/status" | grep -q '"status":"UP"'; do sleep 5; done

# Token descartável: apaga o anterior (se houver) e cria outro.
curl -fs -u "$SONAR_USER:$SONAR_PASSWORD" -X POST "$SONAR_URL/api/user_tokens/revoke" -d name=palestra >/dev/null || true
TOKEN=$(curl -fs -u "$SONAR_USER:$SONAR_PASSWORD" -X POST "$SONAR_URL/api/user_tokens/generate" \
  -d name=palestra -d type=GLOBAL_ANALYSIS_TOKEN | sed -E 's/.*"token":"([^"]+)".*/\1/')

# Gate "Palestra": olha o código todo, não só o novo. O "Sonar way" só avalia código novo
# e, na primeira análise, aprovaria os dois projetos.
api() { curl -fs -u "$SONAR_USER:$SONAR_PASSWORD" -X POST "$SONAR_URL/api/qualitygates/$1" "${@:2}" >/dev/null; }
if api create -d name=Palestra; then
  api create_condition -d gateName=Palestra -d metric=software_quality_reliability_rating -d op=GT -d error=1
  api create_condition -d gateName=Palestra -d metric=software_quality_security_rating -d op=GT -d error=1
  api create_condition -d gateName=Palestra -d metric=coverage -d op=LT -d error=70
  api create_condition -d gateName=Palestra -d metric=duplicated_lines_density -d op=GT -d error=3
fi
api set_as_default -d name=Palestra

(cd exemplo-ideal && ./mvnw -q clean verify sonar:sonar \
  -Dsonar.host.url="$SONAR_URL" -Dsonar.token="$TOKEN")

# O ruim tem testes falhando de propósito: ignora as falhas para gerar o relatório.
(cd exemplo-ruim && ./mvnw -q clean verify sonar:sonar -Dmaven.test.failure.ignore=true \
  -Dsonar.host.url="$SONAR_URL" -Dsonar.token="$TOKEN")

echo
echo "Pronto: $SONAR_URL/dashboard?id=palestra-ideal"
echo "        $SONAR_URL/dashboard?id=palestra-ruim"
