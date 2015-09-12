package jlifx.packet;

public class PowerManagementPacket extends Packet {
    public static final byte TYPE = 0x15;

    public PowerManagementPacket(Packet packet) {
        super(packet);
    }

    public PowerManagementPacket(byte[] targetMacAddress, boolean on) {
        setType(TYPE);
        setTargetMac(targetMacAddress);
        byte[] payload = new byte[2];
        payload[0] = (byte)(on ? 0x01 : 0x00);
        payload[1] = 0x00;
        setPayload(payload);
    }

    public boolean on() {
        return getPayload()[0] == 0x1;
    }

}