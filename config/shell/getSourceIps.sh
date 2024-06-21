#!/bin/bash

port=$1

# 获取含有8080端口的规则
rules=$(firewall-cmd --list-rich-rules | grep $port)

# 初始化一个空数组
addresses=()

# 解析每行并提取source address
while IFS= read -r line; do
    address=$(echo "$line" | awk -F 'source address=' '{print $2}' | awk '{print $1}' | tr -d '"')
    addresses+=("$address")
done <<< "$rules"

# 输出字符串数组
echo "${addresses[@]}"
