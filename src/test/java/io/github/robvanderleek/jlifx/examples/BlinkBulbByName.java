package io.github.robvanderleek.jlifx.examples;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.commandline.command.BlinkCommand;

import java.io.IOException;
import java.util.Optional;

public class BlinkBulbByName {

    public static void main(String[] args) throws IOException {
        Optional<Bulb> optionalBulb = BulbDiscoveryService.discoverBulbByName("Kitchen Tiles");
        optionalBulb.ifPresent(b -> {
            new BlinkCommand().blinkBulbs(3, b);
            b.disconnect();
        });
    }

}