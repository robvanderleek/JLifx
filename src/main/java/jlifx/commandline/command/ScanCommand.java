package jlifx.commandline.command;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import jlifx.bulb.Bulb;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;
import jlifx.commandline.CommandLineCommand;

public class ScanCommand implements CommandLineCommand {

    public boolean execute(String[] args, PrintStream out) throws IOException {
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        if (gatewayBulb == null) {
            out.println("No LIFX gateway bulb found!");
            System.exit(1);
        } else {
            out.println("Found LIFX gateway bulb:");
            out.println("IP address  : " + gatewayBulb.getInetAddress().getHostAddress());
            out.println("Mac address : " + gatewayBulb.getMacAddressAsString());
            Collection<Bulb> allBulbs = DiscoveryService.discoverAllBulbs(gatewayBulb);
            out.println("Found " + allBulbs.size() + " bulb(s) in network:");
            for (Bulb bulb : allBulbs) {
                out.println("Name        : " + bulb.getName());
                out.println("Mac address : " + bulb.getMacAddressAsString());
            }
        }
        return true;
    }

}