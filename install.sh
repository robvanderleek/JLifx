#!/bin/sh
TARGET_DIR="."
mvn clean compile assembly:single
cp target/*with-dependencies.jar $TARGET_DIR/jlifx.jar
echo "Launchable jar (jlifx.jar) copied to $TARGET_DIR"

which jar2sh > /dev/null
if [ "$?" == "0" ]
then
	jar2sh $TARGET_DIR/jlifx $TARGET_DIR/jlifx.jar
	echo "Executable shell script (jlifx) copied to $TARGET_DIR"
fi

echo "Installation done."
