package io.github.robvanderleek.jlifx.packet;

public class GroupRequestPacket extends Packet {

    public GroupRequestPacket(){
        setType((byte)0x33);
    }

    @Override
    public byte getResponseType() {
        return 0x35;
    }

}
