package io.github.robvanderleek.jlifx.examples;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.common.Color;

import java.io.IOException;
import java.util.Optional;

public class ColorBulbByName {

    public static void main(String[] args) throws IOException {
        Optional<Bulb> optionalBulb = BulbDiscoveryService.discoverBulbByName("Kitchen Tiles");
        if (optionalBulb.isPresent()) {
            Bulb bulb = optionalBulb.get();
            bulb.colorize(Color.RED, 0, 1);
            bulb.disconnect();
        }
    }

}