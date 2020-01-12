package io.github.robvanderleek.jlifx.examples;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.common.Color;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Optional;

public class ColorBulbByIpAddress {
    private static final Log LOG = LogFactory.getLog(ColorBulbByIpAddress.class);

    public static void main(String[] args) {
        Optional<Bulb> optionalBulb = BulbDiscoveryService.discoverBulbByIpAddress("192.168.178.50");
        optionalBulb.ifPresent(b -> {
            try {
                b.colorize(Color.BLUE, 0, 1);
            } catch (IOException e) {
                LOG.error(e);
            }
            b.disconnect();
        });
    }
}
