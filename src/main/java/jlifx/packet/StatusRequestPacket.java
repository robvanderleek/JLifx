package jlifx.packet;

public class StatusRequestPacket extends Packet {

    public StatusRequestPacket() {
        setType((byte)0x65);
        setPayload(new byte[] {0x00, 0x00});
    }

}