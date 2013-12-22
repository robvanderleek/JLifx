package jlifx.commandline;

import java.awt.Color;

public class Utils {

    public static byte[] parseMacAddress(String macAddress) {
        byte[] result = new byte[6];
        result[0] = (byte)(Integer.parseInt(macAddress.substring(0, 2), 16));
        result[1] = (byte)(Integer.parseInt(macAddress.substring(3, 5), 16));
        result[2] = (byte)(Integer.parseInt(macAddress.substring(6, 8), 16));
        result[3] = (byte)(Integer.parseInt(macAddress.substring(9, 11), 16));
        result[4] = (byte)(Integer.parseInt(macAddress.substring(12, 14), 16));
        result[5] = (byte)(Integer.parseInt(macAddress.substring(15, 17), 16));
        return result;
    }

    public static Color stringToColor(String string) {
        if (string.equalsIgnoreCase("red")) {
            return Color.red;
        } else if (string.equalsIgnoreCase("green")) {
            return Color.green;
        } else if (string.equalsIgnoreCase("blue")) {
            return Color.blue;
        } else {
            try {
                return Color.decode("#" + string);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
