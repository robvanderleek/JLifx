package jlifx.boblightd;

import java.util.Collection;

import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;
import jlifx.bulb.IBulb;

public class BoblightDaemon {

    public void run() throws Exception {
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        final Collection<IBulb> bulbs = DiscoveryService.discoverAllBulbs(gatewayBulb);
        NetworkUtils.startTcpServer(19333, new BoblightProtocolHandler(bulbs));
        
    }

}