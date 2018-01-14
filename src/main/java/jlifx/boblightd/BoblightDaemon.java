package jlifx.boblightd;

import java.util.Collection;

import io.netty.channel.ChannelFuture;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;
import jlifx.bulb.IBulb;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BoblightDaemon {
    private static final Log LOG = LogFactory.getLog(BoblightDaemon.class);

    public void run() throws Exception {
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        if (gatewayBulb == null) {
            LOG.fatal("Could not find gateway bulb!");
            return;
        }
        final Collection<IBulb> bulbs = DiscoveryService.discoverAllBulbs(gatewayBulb);
        ChannelFuture channelFuture = NetworkUtils.startTcpServer(19333, new BoblightProtocolHandler(bulbs));
        channelFuture.channel().closeFuture().sync();
    }

}