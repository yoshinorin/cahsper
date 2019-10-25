#!/bin/sh

export CAHSPER_DB_DATASOURCE_URL="jdbc:mariadb://127.0.0.1/cahsper?useUnicode=true&characterEncoding=utf8mb4"
export CAHSPER_DB_USER="root"
export CAHSPER_DB_PASSWORD="pass"
export CAHSPER_DB_CONNECTION_TIMEOUT=30000
export CAHSPER_DB_MAXIMUM_POOLSIZE=6

export CAHSPER_HTTP_BIND_ADDRESS="0.0.0.0"
export CAHSPER_HTTP_PORT=9001

export CAHSPER_AWS_COGNITO_ISS="https://cognito-idp.{region}.amazonaws.com/{userPoolId}"
export CAHSPER_AWS_COGNITO_APP_CLIENT_ID "cognito_application_client_id"
