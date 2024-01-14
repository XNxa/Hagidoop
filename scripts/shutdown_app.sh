#!/bin/bash

usage="bash scripts/deploy_servers.sh <config_path>"

IFS=':'


# Build ports list
while read -r line; do
    read -a strarr <<< "$line"
    machine=${strarr[0]}
    port=${strarr[1]}
    workerPort=${strarr[2]}
    echo $machine
    echo $port
    if [ "$machine" = "localhost" ]; then
        `fuser -k $port/tcp` &> /dev/null
        `fuser -k $workerPort/tcp` &> /dev/null
    else 
        echo "ssh $machine "`fuser -k $port/tcp` &> /dev/null"" | bash &
        echo "ssh $machine "`fuser -k $workerPort/tcp` &> /dev/null"" | bash &
    fi
done < $1


        
