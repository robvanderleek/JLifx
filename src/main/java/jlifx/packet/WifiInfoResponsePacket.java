package jlifx.packet;

public class WifiInfoResponsePacket {
    private final Packet packet;

    public WifiInfoResponsePacket(Packet packet) {
        this.packet = packet;
    }

    public float getSignalStrength() {
        return 0; // TODO
    }
}
