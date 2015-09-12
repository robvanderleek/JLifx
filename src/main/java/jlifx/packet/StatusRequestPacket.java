package jlifx.packet;

public class StatusRequestPacket extends Packet {
    public static final byte TYPE = 0x65;

    public StatusRequestPacket() {
        setType(TYPE);
        setPayload(new byte[] {0x00, 0x00});
    }

}