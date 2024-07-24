#!/bin/bash

cd target
java -jar -Dspring.profiles.active=dev moat-0.0.1-SNAPSHOT.jar
