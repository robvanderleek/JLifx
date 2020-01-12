package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;
import io.github.robvanderleek.jlifx.commandline.Utils;
import io.github.robvanderleek.jlifx.common.Color;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

public class ColorCommand extends AbstractBulbCommand {
    private static final Log LOG = LogFactory.getLog(ColorCommand.class);

    @Override
    public boolean execute(Collection<Bulb> bulbs, String[] commandArgs, PrintStream out) {
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

    private void colorizeBulbs(Collection<Bulb> bulbs, Color color, float brightness) {
        for (Bulb bulb : bulbs) {
            try {
                bulb.colorize(color, 3, brightness);
            } catch (IOException e) {
                LOG.error(e);
            }
        }
    }
}