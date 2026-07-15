#!/usr/bin/env bash
set -euo pipefail

API_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MVNW="${API_DIR}/mvnw"

usage() {
  cat <<'EOF'
Uso: ./run-api.sh [debug] [args maven...]

  (sem args)  Sobe a API Spring Boot em modo desenvolvimento
  debug       Sobe com debug remoto na porta 5008 (attach na IDE)
  args extras Repassados ao Maven (ex.: -Dspring-boot.run.profiles=dev)

Exemplos:
  ./run-api.sh
  ./run-api.sh debug
  ./run-api.sh -DskipTests
EOF
}

if [[ "${1:-}" == "-h" || "${1:-}" == "--help" ]]; then
  usage
  exit 0
fi

if [[ ! -f "${MVNW}" ]]; then
  echo "Erro: Maven Wrapper nao encontrado em ${MVNW}" >&2
  exit 1
fi

if [[ ! -x "${MVNW}" ]]; then
  chmod +x "${MVNW}"
fi

resolve_java() {
  if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
    echo "${JAVA_HOME}/bin/java"
    return
  fi

  command -v java
}

java_bin="$(resolve_java || true)"
if [[ -z "${java_bin}" ]]; then
  echo "Erro: Java nao encontrado. Instale o JDK 17 ou defina JAVA_HOME." >&2
  exit 1
fi

java_version="$("${java_bin}" -version 2>&1 | awk -F '[\"._]' '/version/ {print $2}')"
if [[ "${java_version}" -lt 17 ]]; then
  echo "Erro: a API exige Java 17+. Versao atual: ${java_version}" >&2
  echo "Defina JAVA_HOME apontando para um JDK 17 antes de executar." >&2
  exit 1
fi

mvn_args=(spring-boot:run)

if [[ "${1:-}" == "debug" ]]; then
  shift
  export MAVEN_OPTS="${MAVEN_OPTS:-} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5008"
  echo "Debug remoto habilitado na porta 5008"
fi

cd "${API_DIR}"
exec "${MVNW}" "${mvn_args[@]}" "$@"
