#!/bin/bash

usage="bash deploy_write.sh <config path> <kv|txt> <file>  "


IFS=':'

ports=()

while read -r line; do
    read -a strarr <<< "$line"
    machine=${strarr[0]}
    port=${strarr[1]}
    logs="log_$port.txt"
    ports+=($port)
    if [ "$machine" = "localhost" ]; then
        echo $machine
        echo $port
        # Open a new terminal and launch the server on localhost
        echo "java -cp bin hdfs.HdfsServer $port &> $logs" | bash &
    else
        # TODO: start ssh connection, launch the server.
        echo "Can't launch on another machine with SSH for the moment...! Only use localhost"
    fi

done < $1

java -cp bin hdfs.HdfsClient write $2 $3

for i in "${ports[@]}"
do
    # echo $i
    `fuser -k $i/tcp` &> /dev/null
done 