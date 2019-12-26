#!/usr/bin/env bash
#Used to build and run docker image
./docker.sh
if [ $? -eq 0 ]
then
    docker rm -f boot_personFinancialAccounts-1
    docker run -d -it --restart always -p 8080:8080 --name boot_personFinancialAccounts-1 org.esi.us/docker/boot_personFinancialAccounts-1
fi