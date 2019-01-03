package io.github.robvanderleek.jlifx.commandline.command;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import io.github.robvanderleek.jlifx.bulb.IBulb;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;

public class SwitchCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<IBulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length != 2
            || (!(commandArgs[1].equalsIgnoreCase("on") || commandArgs[1].equalsIgnoreCase("off")))) {
            return false;
        } else {
            powerSwitchBulbs(bulbs, commandArgs[1].equalsIgnoreCase("on") ? true : false);
            return true;
        }
    }

    private void powerSwitchBulbs(Collection<IBulb> bulbs, boolean on) throws IOException {
        for (IBulb bulb : bulbs) {
            if (on) {
                bulb.switchOn();
            } else {
                bulb.switchOff();
            }
        }
    }

}