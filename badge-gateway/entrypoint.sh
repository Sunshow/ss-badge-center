#!/bin/bash
cd ${APP_DIR} && java ${JAVA_OPTS} -cp ".:BOOT-INF/classes:BOOT-INF/lib/*" ${MAIN_CLASS} --spring.profiles.active=${SPRING_PROFILE} --logging.file.name=${LOG_FILE}
