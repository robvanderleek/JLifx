package io.github.robvanderleek.jlifx.packet;

import java.awt.Color;

class ColorManagementPacket extends Packet {

    ColorManagementPacket(MacAddress targetMacAddress, Color color, int fadetime, float brightness) {
        float[] hsbValues = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        setType((byte) 0x66);
        setTargetMac(targetMacAddress);
        byte streaming = 0x00;
        byte[] hueBytes = floatToBytes(hsbValues[0]);
        byte[] saturationBytes = floatToBytes(hsbValues[1]);
        byte[] brightnessBytes = floatToBytes(brightness);
        byte[] kelvin = new byte[]{0x0a, (byte) 0xf0};
        byte[] payload = new byte[]{streaming, //
                hueBytes[1], hueBytes[0], //
                saturationBytes[1], saturationBytes[0], //
                brightnessBytes[1], brightnessBytes[0], //
                kelvin[1], //
                kelvin[0], //
                0x00, (byte) fadetime, 0x00, 0x00};
        setPayload(payload);
    }

    private byte[] floatToBytes(float f) {
        int byteValue = (int) (f * 65535);
        byte[] result = new byte[2];
        result[0] = (byte) (byteValue >> 8);
        result[1] = (byte) byteValue;
        return result;
    }

}