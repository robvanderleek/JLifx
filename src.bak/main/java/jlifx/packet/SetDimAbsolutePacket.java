package jlifx.packet;

public class SetDimAbsolutePacket extends Packet {

    public SetDimAbsolutePacket(float brightness) {
        setType((byte)0x68);
        int value;
        if (Math.floor(brightness) >= 1) {
            value = 0xFFFF;
        } else {
            value = (int)(0xFFFF * brightness);
        }
        byte[] payload = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        payload[0] = (byte)(value >> 8);
        payload[1] = (byte)value;
        payload[2] = 0x10;
        setPayload(payload);
    }

}