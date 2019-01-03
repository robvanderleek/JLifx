package io.github.robvanderleek.jlifx.commandline.command;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import io.github.robvanderleek.jlifx.bulb.IBulb;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;
import io.github.robvanderleek.jlifx.commandline.Utils;

public class ColorCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<IBulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length < 2) {
            return false;
        } else {
            Color color = Utils.stringToColor(commandArgs[1]);
            if (color == null) {
                return false;
            } else {
                if (commandArgs.length >= 3 && Utils.isFloatValue(commandArgs[2])) {
                    colorizeBulbs(bulbs, color, Float.parseFloat(commandArgs[2]));
                } else {
                    colorizeBulbs(bulbs, color, 1.0f);
                }
            }
        }
        return true;
    }

    private void colorizeBulbs(Collection<IBulb> bulbs, Color color, float brightness) throws IOException {
        for (IBulb bulb : bulbs) {
            bulb.colorize(color, 3, brightness);
        }
    }
}