#!/bin/sh
mvn clean install -U -DskipTests -Dskip.unit.tests=true -DskipITs=true
cp target/jlifx-*.jar jlifx
echo "CLI (jlifx) has been built."
