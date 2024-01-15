#!/bin/bash

usage="bash deploy_write.sh <config path>"


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
        echo "ssh $machine "java -cp /home/vmouttea/annee2/PDR/bin hdfs.HdfsServer $port" &> $logs" | bash &
    fi

done < $1


