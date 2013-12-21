#!/bin/sh
mvn compile assembly:single
TARGET="$HOME/Desktop/lifx.jar"
cp target/*with-dependencies.jar $HOME/Desktop/lifx.jar
echo "Launchable jar (lifx.jar) copied to $TARGET"
echo "Installation done."
