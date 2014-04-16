package jlifx.commandline.command;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import jlifx.bulb.IBulb;
import jlifx.commandline.AbstractBulbCommand;
import jlifx.commandline.Utils;

public class ColorCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<IBulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length != 2) {
            return false;
        } else {
            Color color = Utils.stringToColor(commandArgs[1]);
            if (color == null) {
                return false;
            } else {
                colorizeBulbs(bulbs, color);
            }
        }
        return true;
    }

    private void colorizeBulbs(Collection<IBulb> bulbs, Color color) throws IOException {
        for (IBulb bulb : bulbs) {
            bulb.colorize(color, 3);
        }
    }
}