package io.github.robvanderleek.jlifx.commandline.command;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import io.github.robvanderleek.jlifx.bulb.IBulb;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;

public class BlinkCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<IBulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length == 2) {
            int times = Integer.parseInt(commandArgs[1]);
            for (int i = 0; i < times; i++) {
                blinkBulbs(bulbs);
            }
        } else {
            startKeyListenerThread(out);
            while (!isInterrupted()) {
                blinkBulbs(bulbs);
            }
        }
        return true;
    }

    private void blinkBulbs(Collection<IBulb> bulbs) throws IOException, InterruptedException {
        for (IBulb bulb : bulbs) {
            bulb.switchOff();
        }
        Thread.sleep(500);
        for (IBulb bulb : bulbs) {
            bulb.switchOn();
        }
        Thread.sleep(500);
    }

}