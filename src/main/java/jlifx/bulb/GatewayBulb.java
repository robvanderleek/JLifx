package jlifx.bulb;

import jlifx.packet.MacAddress;
import jlifx.packet.PacketService;

import java.net.InetAddress;

public class GatewayBulb extends Bulb {
    private final InetAddress inetAddress;
    private final PacketService packetService;

    public GatewayBulb(InetAddress inetAddress, MacAddress macAddress) {
        super(macAddress);
        this.inetAddress = inetAddress;
        this.packetService = new PacketService();
        setGatewayBulb(this);
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public PacketService getPacketService() {
        return packetService;
    }
}