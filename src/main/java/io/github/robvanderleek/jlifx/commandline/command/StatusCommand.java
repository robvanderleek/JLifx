package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbDiscoveryService;
import io.github.robvanderleek.jlifx.bulb.BulbMeshFirmwareStatus;
import io.github.robvanderleek.jlifx.commandline.CommandLineCommand;
import io.github.robvanderleek.jlifx.commandline.Utils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

public class StatusCommand implements CommandLineCommand {

    @Override
    public boolean execute(String[] args, PrintStream out) throws Exception {
        Iterator<Bulb> bulbIterator = BulbDiscoveryService.discoverBulbs().iterator();
        while (bulbIterator.hasNext()) {
            Bulb bulb = bulbIterator.next();
            printBulbStatus(out, bulb);
            BulbMeshFirmwareStatus firmwareStatus = bulb.getMeshFirmwareStatus();
            out.println("Firmware version : " + firmwareStatus.getVersion());
            out.println("Firmware build time: " + firmwareStatus.getBuildTimestamp());
            if (bulbIterator.hasNext()) {
                out.println();
            }
            bulb.disconnect();
        }
        return true;
    }

    private void printBulbStatus(PrintStream out, Bulb bulb) throws IOException {
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