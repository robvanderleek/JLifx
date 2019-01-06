package io.github.robvanderleek.jlifx.commandline.command;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;

public class SwitchCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<Bulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length != 2
            || (!(commandArgs[1].equalsIgnoreCase("on") || commandArgs[1].equalsIgnoreCase("off")))) {
            return false;
        } else {
            powerSwitchBulbs(bulbs, commandArgs[1].equalsIgnoreCase("on") ? true : false);
            return true;
        }
    }

    private void powerSwitchBulbs(Collection<Bulb> bulbs, boolean on) throws IOException {
        for (Bulb bulb : bulbs) {
            if (on) {
                bulb.switchOn();
            } else {
                bulb.switchOff();
            }
        }
    }

}