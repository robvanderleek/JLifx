package jlifx.bulb;

import java.net.InetAddress;

public class GatewayBulb extends Bulb {
    private final InetAddress inetAddress;

    public GatewayBulb(InetAddress inetAddress, byte[] macAddress) {
        super(macAddress);
        this.inetAddress = inetAddress;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    @Override
    public GatewayBulb getGatewayBulb() {
        return this;
    }

}