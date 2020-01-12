package io.github.robvanderleek.jlifx.examples;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.common.Color;

import java.util.Optional;

public class ColorBulbByIpAddress {
    public static void main(String[] args) {
        Optional<Bulb> optionalBulb = BulbDiscoveryService.discoverBulbByIpAddress("192.168.178.50");
        optionalBulb.ifPresent(b -> {
            b.colorize(Color.BLUE, 0, 1);
            b.disconnect();
        });
    }
}
