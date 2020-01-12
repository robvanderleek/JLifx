package io.github.robvanderleek.jlifx.examples;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.common.Color;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Optional;

public class ColorBulbByName {
    private static final Log LOG = LogFactory.getLog(ColorBulbByName.class);

    public static void main(String[] args) {
        Optional<Bulb> optionalBulb = BulbDiscoveryService.discoverBulbByName("Kitchen Tiles");
        optionalBulb.ifPresent(b -> {
            try {
                b.colorize(Color.RED, 0, 1);
            } catch (IOException e) {
                LOG.error(e);
            }
            b.disconnect();
        });
    }
}
