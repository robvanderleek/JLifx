package jlifx.commandline.command;

import java.io.PrintStream;
import java.util.Iterator;

import jlifx.bulb.BulbMeshFirmwareStatus;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;
import jlifx.bulb.IBulb;
import jlifx.commandline.CommandLineCommand;
import jlifx.commandline.Utils;

public class StatusCommand implements CommandLineCommand {

    @Override
    public boolean execute(String[] args, PrintStream out) throws Exception {
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        if (gatewayBulb == null) {
            out.println("No LIFX gateway bulb found!");
            out.println("");
            return false;
        }

        Iterator<IBulb> bulbIterator = DiscoveryService.discoverAllBulbs(gatewayBulb).iterator();
        while (bulbIterator.hasNext()) {
            IBulb bulb = bulbIterator.next();
            printBulbStatus(out, bulb);
            if (bulbIterator.hasNext()) {
                out.println();
            }
        }
        BulbMeshFirmwareStatus firmwareStatus = gatewayBulb.getMeshFirmwareStatus();
        out.println();
        out.println("Mesh firmware version : " + firmwareStatus.getVersion());
        out.println("Mesh firmware build time: " + firmwareStatus.getBuildTimestamp());
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