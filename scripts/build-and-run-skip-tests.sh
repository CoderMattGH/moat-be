#!/bin/bash

cd $(dirname "$0")
./maven-build-skip-tests.sh && ./run-moat-dev.sh
