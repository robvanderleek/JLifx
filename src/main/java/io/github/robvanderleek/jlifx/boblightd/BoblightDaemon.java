package io.github.robvanderleek.jlifx.boblightd;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.bulb.GatewayBulb;
import io.netty.channel.ChannelFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

public class BoblightDaemon {
    private static final Log LOG = LogFactory.getLog(BoblightDaemon.class);

    public void run() throws Exception {
        GatewayBulb gatewayBulb = BulbDiscoveryService.discoverGatewayBulb();
        if (gatewayBulb == null) {
            LOG.fatal("Could not find gateway bulb!");
            return;
        }
        final Collection<Bulb> bulbs = BulbDiscoveryService.discoverAllBulbs(gatewayBulb);
        ChannelFuture channelFuture = NetworkUtils.startTcpServer(19333, new BoblightProtocolHandler(bulbs));
        channelFuture.channel().closeFuture().sync();
    }

}