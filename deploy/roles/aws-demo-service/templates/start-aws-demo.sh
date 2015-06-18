#!/bin/bash

LOGS_HOME="/var/log/aws-demo"
JAVA_OPTS="-server -d64 -Xms512m -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOGS_HOME"

exec java ${JAVA_OPTS} -jar /opt/aws-demo/bin/aws-demo-service.jar server /etc/aws-demo.yml >> /var/log/aws-demo/sysout.log 2>&1