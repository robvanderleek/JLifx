package jlifx.commandline.command;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import jlifx.bulb.Bulb;
import jlifx.commandline.AbstractBulbCommand;

public class BlinkCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<Bulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (commandArgs.length == 1) {
            int times = Integer.parseInt(commandArgs[0]);
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