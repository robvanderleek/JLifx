#!/bin/sh
mvn compile assembly:single
TARGET="$HOME/Desktop/jlifx.jar"
cp target/*with-dependencies.jar $TARGET
echo "Launchable jar (lifx.jar) copied to $TARGET"
echo "Installation done."
