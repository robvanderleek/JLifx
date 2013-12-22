package jlifx.commandline;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import jlifx.bulb.Bulb;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;

public abstract class AbstractBulbCommand implements CommandLineCommand {

    public boolean execute(String[] args, PrintStream out) throws Exception {
        if (args.length < 2) {
            return false;
        }
        if (args[1].equalsIgnoreCase("all")) {
            GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
            return execute(DiscoveryService.discoverAllBulbs(gatewayBulb), args, out);
        } else {
            Bulb bulb = DiscoveryService.lookupBulb(Utils.parseMacAddress(args[1]));
            return execute(Collections.singletonList(bulb), args, out);
        }
    }

    public abstract boolean execute(List<Bulb> bulbs, String[] args, PrintStream out) throws Exception;

}