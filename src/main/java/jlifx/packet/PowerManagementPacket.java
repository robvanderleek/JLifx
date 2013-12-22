package jlifx.packet;

public class PowerManagementPacket extends Packet {

    public PowerManagementPacket(byte[] targetMacAddress, boolean on) {
        setType((byte)0x15);
        setTargetMac(targetMacAddress);
        byte[] payload = new byte[2];
        payload[0] = (byte)(on ? 0x01 : 0x00);
        payload[1] = 0x00;
        setPayload(payload);
    }

}