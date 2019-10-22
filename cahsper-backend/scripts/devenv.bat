setx CAHSPER_DB_DATASOURCE_URL "jdbc:mariadb://127.0.0.1/cahsper?useUnicode=true&characterEncoding=utf8mb4"
setx CAHSPER_DB_USER "root"
setx CAHSPER_DB_PASSWORD "pass"
setx CAHSPER_DB_CONNECTION_TIMEOUT 30000
setx CAHSPER_DB_MAXIMUM_POOLSIZE 6

setx CAHSPER_HTTP_BIND_ADDRESS "0.0.0.0"
setx CAHSPER_HTTP_PORT 9001

setx CAHSPER_AWS_COGNITO_ISS "https://cognito-idp.{region}.amazonaws.com/{userPoolId}"
setx CAHSPER_AWS_COGNITO_JWK "https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json"
setx CAHSPER_AWS_COGNITO_APP_CLIENT_ID "<cognito_application_client_id>"
