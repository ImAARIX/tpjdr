#!/bin/ash

# shellcheck disable=SC2011
JAR_FILE=${JAR_FILE:-$(ls /app/*.jar | xargs -n 1 basename)}
APP_CONF_DIR="${APP_BASE_DIR}/config"
JAR_FILE_TO_EXECUTE="${APP_BASE_DIR}/${JAR_FILE}"

exec java -Dloader.path="${APP_CONF_DIR}" -Dlog4j.configurationFile="${APP_CONF_DIR}"/log4j2.xml -Dlogging.config="${APP_CONF_DIR}"/log4j2.xml -Djava.security.egd=file:/dev/./urandom "${JAVA_OPTS}" -jar "${JAR_FILE_TO_EXECUTE}"
