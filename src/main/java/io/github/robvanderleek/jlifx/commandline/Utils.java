package io.github.robvanderleek.jlifx.commandline;

import io.github.robvanderleek.jlifx.common.Color;
import io.github.robvanderleek.jlifx.common.MacAddress;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

public final class Utils {

    private Utils() {
    }

    static MacAddress parseMacAddress(String macAddress) {
        byte[] result = new byte[6];
        result[0] = (byte) (Integer.parseInt(macAddress.substring(0, 2), 16));
        result[1] = (byte) (Integer.parseInt(macAddress.substring(3, 5), 16));
        result[2] = (byte) (Integer.parseInt(macAddress.substring(6, 8), 16));
        result[3] = (byte) (Integer.parseInt(macAddress.substring(9, 11), 16));
        result[4] = (byte) (Integer.parseInt(macAddress.substring(12, 14), 16));
        result[5] = (byte) (Integer.parseInt(macAddress.substring(15, 17), 16));
        return new MacAddress(result);
    }

    static byte[] parseIpv4Address(String ipv4Address) {
        byte[] result = new byte[4];
        String[] parts = ipv4Address.split("[.]");
        result[0] = (byte) (Integer.parseInt(parts[0]));
        result[1] = (byte) (Integer.parseInt(parts[1]));
        result[2] = (byte) (Integer.parseInt(parts[2]));
        result[3] = (byte) (Integer.parseInt(parts[3]));
        return result;
    }

    private static final Map<String, Color> COLORS = new HashMap<>();

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

    /**
     * Sleep as we know it but without the checked exception.
     */
    public static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns a string containing the hex value of a word.
     *
     * @param w Word value
     * @return String containing hex value of the word value
     */
    public static String wordToHexString(int w) {
        return "$" + Integer.toHexString(w & 0xFFFF).toUpperCase();
    }

    /**
     * Returns a string containing the string representation of the given MAC address.
     *
     * @param macAddress 6-byte MAC address value
     * @return String with MAC address
     */
    public static String getMacAddressAsString(byte[] macAddress) {
        return String
                .format("%02x:%02x:%02x:%02x:%02x:%02x", macAddress[0], macAddress[1], macAddress[2], macAddress[3],
                        macAddress[4], macAddress[5]);
    }

    /**
     * Returns true if the given string is a valid float value, false otherwise.
     *
     * @param s A string value
     * @return true if the given string is a float value, false otherwise
     */
    public static boolean isFloatValue(String s) {
        boolean result = true;
        try {
            Float.parseFloat(s);
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }

    /**
     * Returns true if the given string is a valid integer value, false otherwise.
     *
     * @param s A string value
     * @return true if the given string is a float value, false otherwise
     */
    public static boolean isIntegerValue(String s) {
        boolean result = true;
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }

    /**
     * Return true if the given string is a valid IPv4 address, false otherwise.
     *
     * @param s A string value
     * @return true if the given string is a valid IPv4 address
     */
    static boolean isValidIpv4Address(String s) {
        return s.matches("(\\d{1,3}[.]){3}\\d{1,3}");
    }

    /**
     * Return true if the given string is a valid MAC address, false otherwise.
     *
     * @param s A string value
     * @return true if the given string is a valid MAC address
     */
    static boolean isValidMacAddress(String s) {
        return s.matches("([0-9A-Fa-f]{2}[:]){5}[0-9A-Fa-f]{2}");
    }

    /**
     * Returns an integer with the value of 32-bits little endian formatted byte quadruple.
     *
     * @param b1 First byte
     * @param b2 Second byte
     * @param b3 Third byte
     * @param b4 Fourth byte
     * @return 32-bits integer value from the 4 LE formatted byte quadruple
     */
    public static int from32bitsLittleEndian(byte b1, byte b2, byte b3, byte b4) {
        return (((b4 << 24) | (b3 << 16) | (b2 << 8)) | b1);
    }

    /**
     * Returns an integer with the value of a 64-bits little endian formatted byte sequence.
     */
    public static long from64bitsLittleEndian(byte[] bytes) {
        Validate.isTrue(bytes.length == 8, "Expected 64-bits value");
        long value = 0;
        value += ((long) bytes[0] & 0xffL);
        value += ((long) bytes[1] & 0xffL) << (8);
        value += ((long) bytes[2] & 0xffL) << (16);
        value += ((long) bytes[3] & 0xffL) << (24);
        value += ((long) bytes[4] & 0xffL) << (32);
        value += ((long) bytes[5] & 0xffL) << (40);
        value += ((long) bytes[6] & 0xffL) << (48);
        value += ((long) bytes[7] & 0xffL) << (56);
        return value;
    }

    public static String getIpAddressAsString(byte[] rawBytes) {
        StringBuilder ipAddress = new StringBuilder();
        for (int i = 0; i < rawBytes.length; i++) {
            ipAddress.append(rawBytes[i] & 0xFF);
            if (i < rawBytes.length - 1) {
                ipAddress.append(".");
            }
        }
        return ipAddress.toString();
    }

}