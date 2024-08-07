#!/bin/bash

cd $(dirname "$0")
cd ../target
java -jar -Dspring.profiles.active=dev *.jar
