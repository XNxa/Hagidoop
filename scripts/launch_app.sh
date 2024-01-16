#!/bin/bash

usage="bash deploy_write.sh <config path>"


IFS=':'



while read -r line; do
    read -a strarr <<< "$line"
    machine=${strarr[0]}
    port=${strarr[1]}
    workerPort=${strarr[2]}
    logs="logs_$port.txt"
    wLogs="workerLogs_$workerPort.txt"
    if [ "$machine" = "localhost" ]; then
        echo $machine
        echo $port
        # Open a new terminal and launch the server on localhost
        echo "java -cp bin hdfs.HdfsServer $port &> $logs" | bash &
        echo "java -cp bin daemon.WorkerImpl $workerPort &> $wLogs" | bash &
    else
        # TODO: start ssh connection, launch the server.
        echo "ssh $machine "java -cp /home/vmouttea/annee2/PDR/bin hdfs.HdfsServer $port" &> $logs" | bash &
        echo "ssh $machine "java -cp /home/xnaxara/annee2/PDR/bin daemon.WorkerImpl $workerPort" &> $wLogs" | bash &
    fi

done < $1


