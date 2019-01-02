package jlifx.commandline.command;

import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;
import jlifx.bulb.IBulb;
import jlifx.commandline.CommandLineCommand;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

public class ScanCommand implements CommandLineCommand {

    public boolean execute(String[] args, PrintStream out) throws IOException {
        out.println("Discovering LIFX gateway bulb...");
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        if (gatewayBulb == null) {
            out.println("No LIFX gateway bulb found!");
            out.println();
            return false;
        } else {
            showGatewayBulbInfo(out, gatewayBulb);
            Collection<IBulb> allBulbs = DiscoveryService.discoverAllBulbs(gatewayBulb);
            out.println("Found " + allBulbs.size() + " bulb(s) in network:");
            for (IBulb bulb : allBulbs) {
                out.println("Bulb name   : " + bulb.getName());
                out.println("MAC address : " + bulb.getMacAddressAsString());
            }
        }
        gatewayBulb.disconnect();
        return true;
    }

    private void showGatewayBulbInfo(PrintStream out, GatewayBulb gatewayBulb) {
        out.println("Found LIFX gateway bulb:");
        out.println("IP address     : " + gatewayBulb.getInetAddress().getHostAddress());
        out.println("GW MAC address : " + gatewayBulb.getMacAddressAsString());
    }

}