package jlifx.commandline.command;

import java.io.PrintStream;
import java.util.Collection;

import jlifx.bulb.Bulb;
import jlifx.commandline.AbstractBulbCommand;
import jlifx.commandline.Utils;

public class StatusCommand extends AbstractBulbCommand {

	@Override
	public boolean execute(Collection<Bulb> bulbs, String[] commandArgs,
			PrintStream out) throws Exception {
		for (Bulb bulb : bulbs) {
			out.println("Bulb name   : " + bulb.getName());
			out.println("MAC address : " + bulb.getMacAddressAsString());
			out.println("Hue         : " + Utils.wordToHexString(bulb.getHue()));
			out.println("Saturation  : "
					+ Utils.wordToHexString(bulb.getSaturation()));
			out.println("Brightness  : "
					+ Utils.wordToHexString(bulb.getBrightness()));
			out.println("Kelvin      : "
					+ Utils.wordToHexString(bulb.getKelvin()));
		}
		return true;
	}
}
