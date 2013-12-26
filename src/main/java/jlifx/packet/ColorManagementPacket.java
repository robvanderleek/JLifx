package jlifx.packet;

import java.awt.Color;

public class ColorManagementPacket extends Packet {

    public ColorManagementPacket(byte[] targetMacAddress, Color color, int fadetime) {
        setType((byte)0x66);
        setTargetMac(targetMacAddress);
        byte streaming = 0x00;
        byte[] colorBytes = colorToBytes(color);
        byte[] saturation = new byte[] {(byte)0xFF, (byte)0xFF}; // 0xFFFF for colors, 0x0000 for whites
        byte[] brightness = new byte[] {(byte)0x80, (byte)0x00}; // bright?
        byte[] kelvin = new byte[] {0x0a, (byte)0xf0};
        byte[] payload = new byte[] {streaming, //
            colorBytes[1], //
            colorBytes[0], //
            saturation[1], //
            saturation[0], //
            brightness[1], //
            brightness[0], // 
            kelvin[1], //
            kelvin[0], //
            0x00, (byte)fadetime, 0x00, 0x00};
        setPayload(payload);
    }

    private byte[] colorToBytes(Color color) {
        float[] hsbValues = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int byteValue = (int)(hsbValues[0] * 65535);
        byte[] result = new byte[2];
        result[0] = (byte)(byteValue >> 8);
        result[1] = (byte)byteValue;
        return result;
    }

}