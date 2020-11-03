package io.github.robvanderleek.jlifx.packet;

import io.github.robvanderleek.jlifx.common.Color;
import io.github.robvanderleek.jlifx.common.MacAddress;

import java.nio.ByteBuffer;

public class SetColorZones extends Packet {

    SetColorZones(MacAddress targetMacAddress, short startIndex, short endIndex, Color color, int fadetime, PacketService.Apply apply, float brightness){
        float[] hsbValues = Color.rgbToHsb(color.getRed(), color.getGreen(), color.getBlue());
        setType(new byte[]{(byte) 0xF5, (byte) 0x01});
        setTargetMac(targetMacAddress);
        byte[] hueBytes = floatToBytes(hsbValues[0]);
        byte[] saturationBytes = floatToBytes(hsbValues[1]);
        byte[] brightnessBytes = floatToBytes(brightness);
        byte[] kelvin = new byte[]{0x0a, (byte) 0xf0};
        byte[] duration = intToBytes(fadetime);
        byte[] payload = new byte[]{(byte) startIndex, (byte) endIndex, //
                hueBytes[1], hueBytes[0], //
                saturationBytes[1], saturationBytes[0], //
                brightnessBytes[1], brightnessBytes[0], //
                kelvin[1], kelvin[0], //
                duration[3], duration[2], duration[1], duration[0], //
                (byte) (apply == PacketService.Apply.NO_APPLY ? 0x0 : apply == PacketService.Apply.APPLY ? 0x1 : 0x2)};

        setPayload(payload);
    }

    private byte[] floatToBytes(float f) {
        int byteValue = (int) (f * 65535);
        byte[] result = new byte[2];
        result[0] = (byte) (byteValue >> 8);
        result[1] = (byte) byteValue;
        return result;
    }

    private byte[] intToBytes(int i){
        return ByteBuffer.allocate(4).putInt(i).array();
    }

}
