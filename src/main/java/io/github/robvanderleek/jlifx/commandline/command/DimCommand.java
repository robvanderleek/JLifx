package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;
import io.github.robvanderleek.jlifx.commandline.Utils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

public class DimCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<Bulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length != 2 || !Utils.isFloatValue(commandArgs[1])) {
            return false;
        } else {
            dimBulbs(bulbs, Float.parseFloat(commandArgs[1]));
            return true;
        }
    }

    private void dimBulbs(Collection<Bulb> bulbs, float brightness) throws IOException {
        for (Bulb bulb : bulbs) {
            bulb.setDim(brightness);
        }
    }

}