package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import static io.github.robvanderleek.jlifx.commandline.Utils.safeSleep;

public class BlinkCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<Bulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length == 2) {
            int times = Integer.parseInt(commandArgs[1]);
            blinkBulbs(times, bulbs);
        } else {
            startKeyListenerThread(out);
            while (isNotInterrupted()) {
                blinkBulbs(bulbs);
            }
        }
        return true;
    }

    public void blinkBulbs(int times, Collection<Bulb> bulbs) {
        blinkBulbs(times, bulbs.toArray(new Bulb[]{}));
    }

    public void blinkBulbs(int times, Bulb... bulbs) {
        for (int i = 0; i < times; i++) {
            blinkBulbs(bulbs);
        }
    }

    public void blinkBulbs(Collection<Bulb> bulbs) {
        blinkBulbs(bulbs.toArray(new Bulb[]{}));
    }

    public void blinkBulbs(Bulb... bulbs) {
        for (Bulb bulb : bulbs) {
            bulb.switchOff();
        }
        safeSleep(500);
        for (Bulb bulb : bulbs) {
            bulb.switchOn();
        }
        safeSleep(500);
    }

}