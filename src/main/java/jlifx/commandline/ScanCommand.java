package jlifx.commandline;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import jlifx.bulb.Bulb;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;

public class ScanCommand implements CommandLineCommand {

    public String getCommandName() {
        return "scan";
    }

    public boolean execute(String[] args, PrintStream out) throws IOException {
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        if (gatewayBulb == null) {
            out.println("No LIFX gateway bulb found!");
        } else {
            out.println("Found LIFX gateway bulb!");
            out.println("IP address : " + gatewayBulb.getInetAddress().getHostAddress());
            out.println("Mac address: " + gatewayBulb.getMacAddressAsString());
        }
        List<Bulb> allBulbs = DiscoveryService.discoverAllBulbs(gatewayBulb);
        out.println("Found " + allBulbs.size() + " bulbs in network:");
        for (Bulb bulb : allBulbs) {
            out.println("Mac address: " + bulb.getMacAddressAsString());
        }
        return true;
    }

}
