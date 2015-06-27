#!/bin/sh
mvn clean compile assembly:single
tools/jar2sh jlifx target/*with-dependencies.jar
echo "Executable shell script (jlifx) has been built."
