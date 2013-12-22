package jlifx.commandline;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import jlifx.bulb.Bulb;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;

import org.apache.commons.lang3.ArrayUtils;

public abstract class AbstractBulbCommand implements CommandLineCommand {

	public boolean execute(String[] args, PrintStream out) throws Exception {
		if (args.length < 2) {
			return false;
		}
		String[] commandArgs;
		if (args.length < 3) {
			commandArgs = new String[] {};
		} else {
			commandArgs = ArrayUtils.subarray(args, 2, args.length);
		}
		if (args[1].equalsIgnoreCase("all")) {
			GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
			return execute(DiscoveryService.discoverAllBulbs(gatewayBulb),
					commandArgs, out);
		} else {
			Bulb bulb = DiscoveryService.lookupBulb(Utils
					.parseMacAddress(args[1]));
			return execute(Collections.singletonList(bulb), commandArgs, out);
		}
	}

	public abstract boolean execute(List<Bulb> bulbs, String[] commandArgs,
			PrintStream out) throws Exception;

}