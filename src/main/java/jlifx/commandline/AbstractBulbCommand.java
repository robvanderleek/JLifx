package jlifx.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;

import jlifx.bulb.Bulb;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;

import org.apache.commons.lang3.ArrayUtils;

public abstract class AbstractBulbCommand implements CommandLineCommand {
    private boolean interrupted = false;

    private class KeyListener implements Runnable {
        public void run() {
            try {
                new BufferedReader(new InputStreamReader(System.in)).readLine();
            } catch (IOException e) {
                interrupted = true;
            }
            interrupted = true;
        }
    }

    protected boolean isInterrupted() {
        return interrupted;
    }

    protected void startKeyListenerThread(PrintStream out) {
        out.println("Press [ENTER] to stop");
        new Thread(new KeyListener()).start();
    }

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
            return execute(DiscoveryService.discoverAllBulbs(gatewayBulb), commandArgs, out);
        } else {
            byte[] parseMacAddress = Utils.parseMacAddress(args[1]);
            if (parseMacAddress == null) {
                return false;
            }
            Bulb bulb = DiscoveryService.lookupBulb(parseMacAddress);
            return execute(Collections.singletonList(bulb), commandArgs, out);
        }
    }

    public abstract boolean execute(Collection<Bulb> bulbs, String[] commandArgs, PrintStream out) throws Exception;

}