package jlifx.commandline;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import jlifx.bulb.Bulb;

public class ColorCommand extends AbstractBulbCommand {

	@Override
	public boolean execute(List<Bulb> bulbs, String[] commandArgs,
			PrintStream out) throws Exception {
		if (commandArgs.length != 1) {
			return false;
		} else {
			Color color = Utils.stringToColor(commandArgs[0]);
			if (color == null) {
				return false;
			} else {
				colorizeBulbs(bulbs, color);
			}
		}
		return true;
	}

	private void colorizeBulbs(List<Bulb> bulbs, Color color)
			throws IOException {
		for (Bulb bulb : bulbs) {
			bulb.colorize(color);
		}
	}
}