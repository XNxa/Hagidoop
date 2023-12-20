#!/bin/bash

usage="bash deploy_write.sh <config path> <kv|txt> <file>  "


IFS=':'
while read -r line; do
    read -a strarr <<< "$line"
    machine=${strarr[0]}
    port=${strarr[1]}
    logs="$machine_$port.log"
    if [ "$machine" = "localhost" ]; then
        echo $machine
        echo $port
        # Open a new terminal and launch the server on localhost
        #echo "java -cp bin hdfs.HdfsServer $port &> $logs" | bash &
    else
        # TODO: start ssh connection, launch the server.
        echo "Can't launch on another machine with SSH for the moment...! Only use localhost"
    fi

done < $1

bash & java -cp bin hdfs.HdfsClient write $2 $3

