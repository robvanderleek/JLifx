package jlifx.commandline;

import java.io.PrintStream;
import java.util.List;

import jlifx.bulb.Bulb;

public class BlinkCommand extends AbstractBulbCommand {

	public String getCommandName() {
		return "blink";
	}

	@Override
	public boolean execute(List<Bulb> bulbs, String[] commandArgs,
			PrintStream out) throws Exception {
		while (true) {
			for (Bulb bulb : bulbs) {
				bulb.switchOff();
				Thread.sleep(500);
				bulb.switchOn();
				Thread.sleep(500);
			}
		}
	}

}