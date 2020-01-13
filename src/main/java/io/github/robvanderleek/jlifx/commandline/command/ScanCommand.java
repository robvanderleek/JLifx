package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.commandline.CommandLineCommand;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class ScanCommand implements CommandLineCommand {

    public boolean execute(String[] args, PrintStream out) throws IOException {
        out.println("Discovering LIFX bulbs...");
        List<Bulb> bulbs = BulbDiscoveryService.discoverBulbs();
        if (bulbs.isEmpty()) {
            out.println("No LIFX bulbs found!");
            out.println();
            return false;
        } else {
            out.println("Found " + bulbs.size() + " bulb(s) in network:");
            for (Bulb bulb : bulbs) {
                out.println("Bulb name   : " + bulb.getName());
                out.println("IP address  : " + bulb.getIpAddress());
                bulb.disconnect();
            }
        }
        return true;
    }

}