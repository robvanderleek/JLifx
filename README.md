JLifx
=====

[![BCH compliancy](https://bettercodehub.com/edge/badge/robvanderleek/JLifx)](https://bettercodehub.com)
[![Build Status](https://travis-ci.org/robvanderleek/JLifx.svg?branch=master)](https://travis-ci.org/robvanderleek/JLifx)
[![Known Vulnerabilities](https://snyk.io/test/github/robvanderleek/jlifx/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/robvanderleek/jlifx?targetFile=pom.xml)
[![Coverage Status](https://coveralls.io/repos/robvanderleek/JLifx/badge.svg?branch=master)](https://coveralls.io/r/robvanderleek/JLifx?branch=master)

Small LIFX Wifi LED bulb control utility in Java.

This project is not, in any way, affiliated or related to LIFX Labs.

Implementation is based on the reverse engineered protocol specification by
magicmonkey (https://github.com/magicmonkey/lifxjs/blob/master/Protocol.md)

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

## Development

### Make release

This project uses the Maven release plugin. Before creating a new release make
sure the master branch contains no local changes, then perform these steps:

1. Prepare the release:

    `mvn release:clean release:prepare`
    
2. Peform the release:

    `mvn release:perform`
