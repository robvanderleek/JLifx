package jlifx.packet;

import java.awt.Color;

public class ColorManagementPacket extends Packet {

    public ColorManagementPacket(byte[] targetMacAddress, Color color, int fadetime) {
        setType((byte)0x66);
        setTargetMac(targetMacAddress);
        byte[] colorBytes = colorToBytes(color);
        byte[] payload = new byte[] {0x00, 0x00, colorBytes[0], colorBytes[1], (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, 0x00, 0x00, (byte)fadetime, 0x00, 0x00, 0x00};
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