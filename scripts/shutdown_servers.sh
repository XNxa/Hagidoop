#!/bin/bash

usage="bash scripts/deploy_servers.sh <config_path>"

IFS=':'

ports=()

# Build ports list
while read -r line; do
    read -a strarr <<< "$line"
    port=${strarr[1]}
    ports+=($port)
done < $1


# Kill the process on all ports
for i in "${ports[@]}"
do
    # echo $i
    `fuser -k $i/tcp` &> /dev/null
done 