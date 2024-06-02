#!/bin/bash
scriptPath=$(cd `dirname $0`;pwd)
configPath=$scriptPath/../config

function main(){
  while true; do
cat << EOF
#############################################################
#                  xki manage program                       #
#   1. config ekm info(ip/port)                             #
#   2. manage ip white list                                 #
#   3. manage policy                                        #
#   4. show cert and public key digest                      #
#   0. exit                                                 #
#############################################################
EOF
    read -p "please input option (0-4): " input
    case $input in
      "1")
        configEkm
      ;;
      "2")
        configIpWhiteList
      ;;
      "3")
        configPolicy
      ;;
      "4")
        showDigest
      ;;
      "0")
        break
      ;;
      *)
        echo "input error.please input 0-4."
    esac
  done
}

function configEkm(){
  read -p "please xki port: " xki_port
  read -p "please ekm ip: " ekm_ip
  read -p "please ekm port: " ekm_port

  \cp $configPath/template/application.yml $configPath
  sed -i "s/\[xki_port\]/$xki_port/g" $configPath/application.yml
  sed -i "s/\[ekm_ip\]/$ekm_ip/g" $configPath/application.yml
  sed -i "s/\[ekm_port\]/$ekm_port/g" $configPath/application.yml

  read -p "The above modifications will take effect after restarting the service, restart now? (yes/no): " restartFlag
  if [ "$restartFlag" == "yes" ]; then
      sh $scriptPath/restart.sh
  fi
}

function configIpWhiteList(){
  while true; do
cat << EOF
#############################################################
#                  xki ip white list manage                 #
#   1. show source ips                                      #
#   2. add source ip                                       #
#   3. delete source ip                                     #
#   0. return to the previous menu                          #
#############################################################
EOF
    read -p "please input option (0-3): " input
    xki_port=$(awk '/port:/ {print $2}' "$configPath/application.yml")
    if [[ "$xki_port" =~ [^0-9] ]]; then
        echo "get xki port failed,please check application.yml path or content."
        input="0"
    fi
    case $input in
      "1")
        showSourceIps $xki_port
      ;;
      "2")
        addSourceIp $xki_port
      ;;
      "3")
        deleteSourceIps $xki_port
      ;;
      "0")
        break
      ;;
      *)
        echo "input error.please input 0-3."
    esac
  done
}

function configPolicy(){
  while true; do
cat << EOF
#############################################################
#                  xki policy manage                        #
#   1. show policy list                                     #
#   2. modify policy                                        #
#   0. return to the previous menu                          #
#############################################################
EOF
    read -p "please input option (0-2): " input
    xki_port=$(awk '/port:/ {print $2}' "$configPath/application.yml")
    if [[ "$xki_port" =~ [^0-9] ]]; then
        echo "get xki port failed,please check application.yml path or content."
        input="0"
    fi
    case $input in
      "1")
        showPolicyList $xki_port
      ;;
      "2")
        modifyPolicy $xki_port
      ;;
      "0")
        break
      ;;
      *)
        echo "input error.please input 0-3."
    esac
  done
}

function showDigest(){
  xki_port=$(awk '/port:/ {print $2}' "$configPath/application.yml")
  if [[ "$xki_port" =~ [^0-9] ]]; then
    echo "get xki port failed,please check application.yml path or content."
    return
  fi
  res=$(curl -k -s "https://127.0.0.1:${xki_port}/kms/xki/v1/system/generateCertDigest")

  if [ -n "$res" ] && [[ $res == *\{* ]]; then
      message=$(echo $res | jq '.Message')
      if [[ "$message" == "\"success\"" ]]; then
        echo $res | jq '.data'
      else
        echo $message
      fi
  fi
}

function showSourceIps(){
  xki_port=$1
  res=$(curl -k -s "https://127.0.0.1:${xki_port}/kms/xki/v1/system/configIpWhiteList" \
  --header 'Content-Type: application/json' \
  --data '{
      "oper": "QUERY"
  }')

  if [ -n "$res" ] && [[ $res == *\{* ]]; then
      message=$(echo $res | jq '.Message')
      if [[ "$message" == "\"success\"" ]]; then
        echo $res | jq '.data'
      else
        echo $message
      fi
  fi
}

function addSourceIp(){
  xki_port=$1
  read -p "please input source ip address to ip white list: " ip
  res=$(curl -k -s "https://127.0.0.1:${xki_port}/kms/xki/v1/system/configIpWhiteList" \
  --header 'Content-Type: application/json' \
  --data "{
      \"oper\": \"ADD\",
      \"ip\": \"${ip}\"
  }")
  if [ -n "$res" ] && [[ $res == *\{* ]]; then
    echo $res | jq '.Message'
  fi
}

function deleteSourceIps(){
  xki_port=$1
  read -p "please enter the source ip to remove it from the whitelist: " ip
  res=$(curl -k -s "https://127.0.0.1:${xki_port}/kms/xki/v1/system/configIpWhiteList" \
  --header 'Content-Type: application/json' \
  --data "{
      \"oper\": \"DEL\",
      \"ip\": \"${ip}\"
  }")
  if [ -n "$res" ] && [[ $res == *\{* ]]; then
    echo $res | jq '.Message'
  fi
}


function showPolicyList(){
  xki_port=$1
  res=$(curl -k -s "https://127.0.0.1:${xki_port}/kms/xki/v1/system/queryPolicy")

  if [ -n "$res" ] && [[ $res == *\{* ]]; then
      message=$(echo $res | jq '.Message')
      if [[ "$message" == "\"success\"" ]]; then
        echo $res | jq '.data'
      else
        echo $message
      fi
  fi
}

function modifyPolicy(){
  xki_port=$1
  read -p "please input policy key: " key
  read -p "please input policy value: " value
  res=$(curl -k -s "https://127.0.0.1:${xki_port}/kms/xki/v1/system/configPolicy" \
  --header 'Content-Type: application/json' \
  --data "{
      \"${key}\": \"${value}\"
  }")

  if [ -n "$res" ] && [[ $res == *\{* ]]; then
      echo $res | jq '.Message'
  fi
}

main
