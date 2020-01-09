JLifx
=====

[![BCH compliancy](https://bettercodehub.com/edge/badge/robvanderleek/JLifx)](https://bettercodehub.com)
[![Build Status](https://github.com/robvanderleek/JLifx/workflows/CI/badge.svg)](https://github.com/robvanderleek/JLifx/actions)
[![Build Status](https://github.com/robvanderleek/JLifx/workflows/Release/badge.svg)](https://github.com/robvanderleek/JLifx/actions)
[![Known Vulnerabilities](https://snyk.io/test/github/robvanderleek/jlifx/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/robvanderleek/jlifx?targetFile=pom.xml)
[![Coverage Status](https://coveralls.io/repos/robvanderleek/JLifx/badge.svg?branch=master)](https://coveralls.io/r/robvanderleek/JLifx?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.robvanderleek/jlifx/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.robvanderleek/jlifx)

Small LIFX Wifi LED bulb control library and utility in Java.

This project is not, in any way, affiliated or related to LIFX Labs.

Implementation is based on the reverse engineered protocol specification by
magicmonkey (https://github.com/magicmonkey/lifxjs/blob/master/Protocol.md)

## Usage

Build the JAR and run it from the command line:

	java -jar jlifx.jar

The supported commands are:

    daemon (starts Boblight daemon)
    scan
    status
    switch  <mac-address|bulb name|gateway|all> <on|off>
    color   <mac-address|bulb name|gateway|all> [brightness (0.0 - 1.0)]
    blink   <mac-address|bulb name|gateway|all> [times]
    rainbow <mac-address|bulb name|gateway|all> [duration (sec)]

Examples:

    java -jar jlifx.jar switch all off
    java -jar jlifx.jar color all red
    java -jar jlifx.jar color livingroom red
    java -jar jlifx.jar blink gateway
    java -jar jlifx.jar blink AA:BB:CC:DD:EE:FF 3
    java -jar jlifx.jar rainbow all
    java -jar jlifx.jar rainbow all 30
    
Using the API:

```java
    class Client {
        public static void main(Stringp[] args) {
            GatewayBulb gatewayBulb = BulbDiscoveryService.discoverGatewayBulb();
            Optional<Bulb> livingRoomLight = BulbDiscoveryService.discoverBulbByName(gatewayBulb, "LivingRoomLight");
            livingRoomLight.ifPresent(bulb -> bulb.colorize(Color.BLUE, 0, 1f));
        }
    }
```

## Installation

For Maven, add the following entry to your `pom.xml`:

```xml
    ...
    <dependencies>
        ...
        <dependency>
            <groupId>io.github.robvanderleek</groupId>
            <artifactId>jlifx</artifactId>
            <version>0.5.0</version>
        </dependency>
    </dependencies>
    ...
```

## Development

### Make release

This project uses the Maven release plugin. Before creating a new release make
sure the master branch contains no local changes, then perform these steps:

1. Prepare the release:

    `mvn release:clean release:prepare`
    
2. Peform the release:

    `mvn release:perform`
