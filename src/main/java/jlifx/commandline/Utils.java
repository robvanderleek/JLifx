package jlifx.commandline;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static byte[] parseMacAddress(String macAddress) {
        try {
            byte[] result = new byte[6];
            result[0] = (byte)(Integer.parseInt(macAddress.substring(0, 2), 16));
            result[1] = (byte)(Integer.parseInt(macAddress.substring(3, 5), 16));
            result[2] = (byte)(Integer.parseInt(macAddress.substring(6, 8), 16));
            result[3] = (byte)(Integer.parseInt(macAddress.substring(9, 11), 16));
            result[4] = (byte)(Integer.parseInt(macAddress.substring(12, 14), 16));
            result[5] = (byte)(Integer.parseInt(macAddress.substring(15, 17), 16));
            return result;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static final Map<String, Color> COLORS = new HashMap<String, Color>();
    static {
        COLORS.put("black", Color.BLACK);
        COLORS.put("blue", Color.BLUE);
        COLORS.put("cyan", Color.CYAN);
        COLORS.put("darkgray", Color.DARK_GRAY);
        COLORS.put("gray", Color.GRAY);
        COLORS.put("green", Color.GREEN);
        COLORS.put("lightgrey", Color.LIGHT_GRAY);
        COLORS.put("magenta", Color.MAGENTA);
        COLORS.put("orange", Color.ORANGE);
        COLORS.put("pink", Color.PINK);
        COLORS.put("red", Color.RED);
        COLORS.put("white", Color.WHITE);
        COLORS.put("yellow", Color.YELLOW);
    }

    public static Color stringToColor(String string) {
        Color color = COLORS.get(string);
        if (color != null) {
            return color;
        } else {
            try {
                return Color.decode("#" + string);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
