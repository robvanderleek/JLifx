package jlifx.bulb;

import java.net.InetAddress;

import jlifx.commandline.Utils;

public class GatewayBulb extends Bulb {
    private final InetAddress inetAddress;
    private final byte[] gatewayMacAddress;

    public GatewayBulb(InetAddress inetAddress, byte[] macAddress, byte[] gatewayMacAddress) {
        super(macAddress);
        this.inetAddress = inetAddress;
        this.gatewayMacAddress = gatewayMacAddress;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    @Override
    public GatewayBulb getGatewayBulb() {
        return this;
    }

    public byte[] getGatewayMacAddress() {
        return gatewayMacAddress;
    }

    public String getGatewayMacAddressAsString() {
        return Utils.getMacAddressAsString(gatewayMacAddress);
    }

}