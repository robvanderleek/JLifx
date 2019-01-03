package io.github.robvanderleek.jlifx.packet;

public class WifiInfoResponsePacket extends Packet {

    public WifiInfoResponsePacket(Packet packet) {
        super(packet);
    }

    public float getSignalStrength() {
        return 0; // TODO
    }

}