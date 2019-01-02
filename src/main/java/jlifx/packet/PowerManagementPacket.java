package jlifx.packet;

class PowerManagementPacket extends Packet {
    static final byte TYPE = 0x15;

    PowerManagementPacket(Packet packet) {
        super(packet);
    }

    PowerManagementPacket(MacAddress targetMacAddress, boolean on) {
        setType(TYPE);
        setTargetMac(targetMacAddress);
        byte[] payload = new byte[2];
        payload[0] = (byte) (on ? 0xFF : 0x00);
        payload[1] = (byte) (on ? 0xFF : 0x00);
        setPayload(payload);
    }

    boolean on() {
        byte[] payload = getPayload();
        return payload[0] == (byte) 0xFF && payload[1] == (byte) 0xFF;
    }
}