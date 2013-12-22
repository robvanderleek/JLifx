#!/bin/sh
TARGET_DIR="$HOME/Desktop/"
mvn clean compile assembly:single
cp target/*with-dependencies.jar $TARGET_DIR/jlifx.jar
./jar2sh "$TARGET_DIR/jlifx" $TARGET_DIR/jlifx.jar
echo "Launchable jar (jlifx.jar) copied to $TARGET_DIR"
echo "Executable shell script (jlifx) copied to $TARGET_DIR"
echo "Installation done."
