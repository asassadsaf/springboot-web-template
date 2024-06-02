#!/bin/bash
scriptPath=$(cd `dirname $0`;pwd)
jarPath=$scriptPath/..
nohup java -Xmx512M -Xms512M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$jarPath/logs/heap.dump -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$1 -jar $jarPath/seckms-xkip-1.0.0.jar >/dev/null 2>&1 &
echo seckms-xkip starting...
