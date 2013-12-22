package jlifx.commandline;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import jlifx.bulb.Bulb;

public class SwitchCommand extends AbstractBulbCommand {

    public String getCommandName() {
        return "switch";
    }

    @Override
    public boolean execute(List<Bulb> bulbs, String[] args, PrintStream out) throws Exception {
        if (args.length != 3 || (!(args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("off")))) {
            return false;
        } else {
            powerSwitchBulbs(bulbs, args[2].equalsIgnoreCase("on") ? true : false);
            return true;
        }
    }

    private void powerSwitchBulbs(List<Bulb> bulbs, boolean on) throws IOException {
        for (Bulb bulb : bulbs) {
            if (on) {
                bulb.switchOn();
            } else {
                bulb.switchOff();
            }
        }
    }

}