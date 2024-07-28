#!/bin/bash

cd $(dirname "$0")
cd ..
mvn clean package -Dmaven.test.skip=true
