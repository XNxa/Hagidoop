#!/bin/bash

usage="bash scripts/clean.sh <config_path>"

IFS=':'


# Build ports list
while read -r line; do
    read -a strarr <<< "$line"
    machine=${strarr[0]}
    portHdfs=${strarr[1]}
    portRmi=${strarr[2]}
    if [ "$machine" = "localhost" ]; then
        rm -rf /tmp/HDFS_V_X
    else 
        echo "ssh $machine rm -rf /tmp/HDFS_V_X" | bash &
    fi
done < $1