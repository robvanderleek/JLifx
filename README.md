JLifx
=====

Small LIFX Wifi LED bulb control utility in Java.

Usage
-----
Build the JAR and run it from the command line:

	java -jar jlifx.jar

The supported commands are:

	daemon (starts Boblight daemon)
	scan
	status  <mac-address|all>
	switch  <mac-address|all> <on|off>
	color   <mac-address|all> <color-name|rgb-hex-value>
	blink   <mac-address|all> [times]
	rainbow <mac-address|all>

Examples:

	java -jar jlifx.jar switch all off
	java -jar jlifx.jar color all red
	java -jar jlifx.jar blink AA:BB:CC:DD:EE:FF 3
	java -jar jlifx.jar rainbow all
