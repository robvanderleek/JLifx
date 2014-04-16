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
import jlifx.bulb.IBulb;

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

    @Override
    public boolean execute(String[] args, PrintStream out) throws Exception {
        if (args.length < 2) {
            return false;
        }
        String[] commandArgs = getCommandArgs(args);
        GatewayBulb gatewayBulb;
        gatewayBulb = DiscoveryService.discoverGatewayBulb();
        return dispatchExecute(gatewayBulb, commandArgs, out);
    }

    private boolean dispatchExecute(GatewayBulb gatewayBulb, String[] commandArgs, PrintStream out) throws IOException,
        Exception {
        if (commandArgs[0].equalsIgnoreCase("all")) {
            return executeForAllBulbs(gatewayBulb, commandArgs, out);
        } else if (commandArgs[0].equalsIgnoreCase("gateway")) {
            return executeForGatewayBulb(gatewayBulb, commandArgs, out);
        } else {
            return executeForSingleBulb(gatewayBulb, commandArgs, out);
        }
    }

    private boolean executeForAllBulbs(GatewayBulb gatewayBulb, String[] commandArgs, PrintStream out)
        throws IOException, Exception {
        return execute(DiscoveryService.discoverAllBulbs(gatewayBulb), commandArgs, out);
    }

    private boolean executeForGatewayBulb(GatewayBulb gatewayBulb, String[] commandArgs, PrintStream out)
        throws IOException, Exception {
        return execute(Collections.singletonList((IBulb)gatewayBulb), commandArgs, out);
    }

    private boolean executeForSingleBulb(GatewayBulb gatewayBulb, String[] commandArgs, PrintStream out)
        throws IOException, Exception {
        byte[] macAddress = Utils.parseMacAddress(commandArgs[0]);
        if (macAddress == null) {
            return false;
        }
        IBulb bulb = new Bulb(macAddress, gatewayBulb);
        return execute(Collections.singletonList(bulb), commandArgs, out);
    }

    private String[] getCommandArgs(String[] args) {
        String[] commandArgs;
        if (args.length < 2) {
            commandArgs = new String[] {};
        } else {
            commandArgs = ArrayUtils.subarray(args, 1, args.length);
        }
        return commandArgs;
    }

    public abstract boolean execute(Collection<IBulb> bulbs, String[] commandArgs, PrintStream out) throws Exception;

}