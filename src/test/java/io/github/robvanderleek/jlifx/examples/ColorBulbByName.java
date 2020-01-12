package io.github.robvanderleek.jlifx.examples;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.common.Color;

import java.util.Optional;

public class ColorBulbByName {
    public static void main(String[] args) {
        Optional<Bulb> optionalBulb = BulbDiscoveryService.discoverBulbByName("Kitchen Tiles");
        optionalBulb.ifPresent(b -> {
            b.colorize(Color.RED, 0, 1);
            b.disconnect();
        });
    }
}
