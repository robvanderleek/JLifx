package jlifx.commandline;

import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import jlifx.bulb.Bulb;

public class RainbowCommand extends AbstractBulbCommand {

	@Override
	public boolean execute(List<Bulb> bulbs, String[] commandArgs,
			PrintStream out) throws Exception {
		List<Color> spectrumColors = getSpectrumColors();
		int i = 0;
		while (true) {
			for (Bulb bulb : bulbs) {
				Color color = spectrumColors.get(i++);
				bulb.colorize(color);
				Thread.sleep(50);
			}
			if (i >= spectrumColors.size()) {
				i = 0;
			}
		}
	}

	private List<Color> getSpectrumColors() {
		List<Color> colors = new ArrayList<Color>();
		for (int i = 0; i < 512; i++) {
			double factor = ((2 * Math.PI) * i) / 512;
			int red = (int) (Math.sin(factor + 0) * 127 + 128);
			int green = (int) (Math.sin(factor + 2) * 127 + 128);
			int blue = (int) (Math.sin(factor + 4) * 127 + 128);
			colors.add(new Color(red, green, blue));
		}
		return colors;
	}
}
