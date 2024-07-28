#!/bin/bash

cd $(dirname "$0")

echo WARNING!
echo This script will delete all data in the MOAT production and development databases if they exist.
read -p "Do you wish to proceed? (yes/no) " yn

case $yn in
   
   yes ) echo Proceeding with DB setup...;;
   no ) echo Exiting...;
        exit;;
   * ) echo Invalid response!
       exit 1;;

esac

psql -f setup.sql
