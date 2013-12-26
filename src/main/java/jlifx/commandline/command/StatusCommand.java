package jlifx.commandline.command;

import java.io.PrintStream;
import java.util.Collection;

import jlifx.bulb.Bulb;
import jlifx.commandline.AbstractBulbCommand;

public class StatusCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<Bulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        for (Bulb bulb : bulbs) {
            out.println("Bulb name   : " + bulb.getName());
            out.println("MAC address : " + bulb.getMacAddressAsString());
            out.println("Hue         : " + bulb.getHue());
        }
        return true;
    }
}
