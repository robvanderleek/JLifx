package io.github.robvanderleek.jlifx.commandline;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractBulbCommand implements CommandLineCommand {
    private boolean interrupted = false;

    private class KeyListener implements Runnable {
        @Override
        public void run() {
            try {
                new BufferedReader(new InputStreamReader(System.in)).readLine();
            } catch (IOException e) {
                interrupted = true;
            }
            interrupted = true;
        }
    }

    private class Timer implements Runnable {
        private final int duration;

        Timer(int duration) {
            this.duration = duration;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(duration * 1000);
            } catch (InterruptedException e) {
                interrupted = true;
            }
            interrupted = true;
        }

    }

    protected boolean isNotInterrupted() {
        return !interrupted;
    }

    protected void startKeyListenerThread(PrintStream out) {
        out.println("Press [ENTER] to stop");
        new Thread(new KeyListener()).start();
    }

    protected void startTimer(int duration) {
        new Thread(new Timer(duration)).start();
    }

    @Override
    public boolean execute(String[] args, PrintStream out) throws Exception {
        if (args.length < 2) {
            return false;
        }
        String[] commandArgs = getCommandArgs(args);
        return dispatchExecute(commandArgs, out);
    }

    private boolean dispatchExecute(String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs[0].equalsIgnoreCase("all")) {
            return executeForAllBulbs(commandArgs, out);
        } else {
            return executeForSingleBulb(commandArgs, out);
        }
    }

    private boolean executeForAllBulbs(String[] commandArgs, PrintStream out) throws Exception {
        List<Bulb> bulbs = BulbDiscoveryService.discoverBulbs();
        boolean result = execute(bulbs, commandArgs, out);
        bulbs.forEach(Bulb::disconnect);
        return result;
    }

    private boolean executeForSingleBulb(String[] commandArgs, PrintStream out) throws Exception {
        Bulb bulb;
        if (Utils.isValidIpv4Address(commandArgs[0])) {
            bulb = BulbDiscoveryService.discoverBulbByIpAddress(commandArgs[0])
                                       .orElseThrow(() -> new RuntimeException("Bulb not found: " + commandArgs[0]));
        } else {
            bulb = BulbDiscoveryService.discoverBulbByName(commandArgs[0])
                                       .orElseThrow(() -> new RuntimeException("Bulb not found: " + commandArgs[0]));
        }
        boolean result = execute(Collections.singletonList(bulb), commandArgs, out);
        bulb.disconnect();
        return result;
    }

    private String[] getCommandArgs(String[] args) {
        String[] commandArgs;
        if (args.length < 2) {
            commandArgs = new String[]{};
        } else {
            commandArgs = ArrayUtils.subarray(args, 1, args.length);
        }
        return commandArgs;
    }

    public abstract boolean execute(Collection<Bulb> bulbs, String[] commandArgs, PrintStream out) throws Exception;

}