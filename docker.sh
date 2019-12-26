#!/usr/bin/env bash

#Used to build docker image
mvn package
if [ $? -eq 1 ]
then
    exit 1
else
    docker build -t org.esi.us/docker/boot_personFinancialAccounts-1 .
fi