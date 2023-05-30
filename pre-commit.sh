#!/bin/sh

cd ./scala
sbt scalastyle

if [ $? -ne 0 ]; then
    echo "Scalastyle error"
    exit 1
fi