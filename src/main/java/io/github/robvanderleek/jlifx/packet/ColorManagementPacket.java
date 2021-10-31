package io.github.robvanderleek.jlifx.packet;

import io.github.robvanderleek.jlifx.common.Color;
import io.github.robvanderleek.jlifx.common.MacAddress;

class ColorManagementPacket extends Packet {

    ColorManagementPacket(MacAddress targetMacAddress, Color color, int fadetime, float brightness) {
        float[] hsbValues = Color.rgbToHsb(color.getRed(), color.getGreen(), color.getBlue());
        setType((byte) 0x66);
        setTargetMac(targetMacAddress);
        byte streaming = 0x00;
        byte[] hueBytes = floatToBytes(hsbValues[0]);
        byte[] saturationBytes = floatToBytes(hsbValues[1]);
        byte[] brightnessBytes = floatToBytes(brightness);
        byte[] kelvin = new byte[]{0x0a, (byte) 0xf0};
        byte[] duration = intToBytes(fadetime);
        byte[] payload = new byte[]{streaming, //
                hueBytes[1], hueBytes[0], //
                saturationBytes[1], saturationBytes[0], //
                brightnessBytes[1], brightnessBytes[0], //
                kelvin[1], //
                kelvin[0], //
                duration[3], duration[2], duration[1], duration[0]};
        setPayload(payload);
    }

}