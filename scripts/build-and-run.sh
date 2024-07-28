#!/bin/bash

cd $(dirname "$0")
./maven-build.sh && ./run-moat-dev.sh
