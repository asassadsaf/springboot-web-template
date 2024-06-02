#!/bin/bash
scriptPath=$(cd `dirname $0`;pwd)
jarPath=$scriptPath/..
nohup java -Xmx512M -Xms512M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$jarPath/logs/heap.dump -jar $jarPath/seckms-xkip-1.0.0.jar >/dev/null 2>&1 &
echo seckms-xkip starting...
