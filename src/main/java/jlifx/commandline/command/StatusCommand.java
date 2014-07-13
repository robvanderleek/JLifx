package jlifx.commandline.command;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

import jlifx.bulb.BulbMeshFirmwareStatus;
import jlifx.bulb.IBulb;
import jlifx.commandline.AbstractBulbCommand;
import jlifx.commandline.Utils;

public class StatusCommand extends AbstractBulbCommand {

    @Override
    public boolean execute(Collection<IBulb> bulbs, String[] commandArgs, PrintStream out) throws Exception {
        if (bulbs.isEmpty()) {
            out.println("No bulbs discovered!");
        } else {
            Iterator<IBulb> bulbIterator = bulbs.iterator();
            while (bulbIterator.hasNext()) {
                IBulb bulb = bulbIterator.next();
                printBulbStatus(out, bulb);
                if (bulbIterator.hasNext()) {
                    out.println();
                }
            }
            IBulb bulb = bulbs.iterator().next();
            BulbMeshFirmwareStatus firmwareStatus = bulb.getGatewayBulb().getMeshFirmwareStatus();
            out.println();
            out.println("Mesh firmware version : " + firmwareStatus.getVersion());
        }
        return true;
    }

    private void printBulbStatus(PrintStream out, IBulb bulb) {
        out.println("Bulb name   : " + bulb.getName());
        out.println("MAC address : " + bulb.getMacAddressAsString());
        out.println("Hue         : " + Utils.wordToHexString(bulb.getHue()));
        out.println("Saturation  : " + Utils.wordToHexString(bulb.getSaturation()));
        out.println("Brightness  : " + Utils.wordToHexString(bulb.getBrightness()));
        out.println("Kelvin      : " + Utils.wordToHexString(bulb.getKelvin()));
        out.println("Dim         : " + Utils.wordToHexString(bulb.getDim()));
        out.println("Power       : " + Utils.wordToHexString(bulb.getPower()));
    }

}