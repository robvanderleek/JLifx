package io.github.robvanderleek.jlifx.packet;

public class WifiInfoRequestPacket extends Packet {

    public WifiInfoRequestPacket() {
        setType((byte)0x10);
    }

}