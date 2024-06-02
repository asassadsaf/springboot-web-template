#!/bin/bash
scriptPath=$(cd `dirname $0`;pwd)

sh $scriptPath/stop.sh
sh $scriptPath/start.sh
