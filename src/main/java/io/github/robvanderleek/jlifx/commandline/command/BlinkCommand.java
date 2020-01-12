package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

public class BlinkCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<Bulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length == 2) {
            int times = Integer.parseInt(commandArgs[1]);
            for (int i = 0; i < times; i++) {
                blinkBulbs(bulbs);
            }
        } else {
            startKeyListenerThread(out);
            while (isNotInterrupted()) {
                blinkBulbs(bulbs);
            }
        }
        return true;
    }

    private void blinkBulbs(Collection<Bulb> bulbs) throws IOException, InterruptedException {
        for (Bulb bulb : bulbs) {
            bulb.switchOff();
        }
        Thread.sleep(500);
        for (Bulb bulb : bulbs) {
            bulb.switchOn();
        }
        Thread.sleep(500);
    }

}