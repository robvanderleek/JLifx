package jlifx.packet;

public class StatusRequestPacket extends Packet {
    static final byte TYPE = 0x65;

    StatusRequestPacket() {
        setType(TYPE);
        setPayload(new byte[]{0x00, 0x00});
    }

    @Override
    public byte getResponseType() {
        return 0x6B;
    }
}