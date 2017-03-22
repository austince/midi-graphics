#!/usr/bin/env bash

# Zip the setup.py, tests, requirements, README.md, and package
# Exclude caches, outputs, and complied files
# Output is in results

if [ $1 ]; then
FILENAME="CAWLEY-EDWARDS_Austin_${1}.zip"
else
FILENAME="CAWLEY-EDWARDS_Austin.zip"
fi

zip -r ${FILENAME} \
	. \
	-x *.git* out *.idea*
