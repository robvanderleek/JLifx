package io.github.robvanderleek.jlifx.examples;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.common.Color;

import java.io.IOException;
import java.util.Optional;

public class ColorBulbByIpAddress {

    public static void main(String[] args) throws IOException {
        Optional<Bulb> optionalBulb = BulbDiscoveryService.discoverBulbByIpAddress("192.168.178.50");
        if (optionalBulb.isPresent()) {
            Bulb bulb = optionalBulb.get();
            bulb.colorize(Color.BLUE, 0, 1);
            bulb.disconnect();
        }
    }

}