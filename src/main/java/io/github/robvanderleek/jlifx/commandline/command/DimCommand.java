package io.github.robvanderleek.jlifx.commandline.command;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import io.github.robvanderleek.jlifx.bulb.IBulb;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;
import io.github.robvanderleek.jlifx.commandline.Utils;

public class DimCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<IBulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length != 2 || !Utils.isFloatValue(commandArgs[1])) {
            return false;
        } else {
            dimBulbs(bulbs, Float.parseFloat(commandArgs[1]));
            return true;
        }
    }

    private void dimBulbs(Collection<IBulb> bulbs, float brightness) throws IOException {
        for (IBulb bulb : bulbs) {
            bulb.setDim(brightness);
        }
    }

}