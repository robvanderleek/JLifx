package jlifx;

import java.net.InetAddress;

public class Bulb {
    private final InetAddress inetAddress;
    private final byte[] macAddress;

    public Bulb(InetAddress inetAddress, byte[] macAddress) {
        this.inetAddress = inetAddress;
        this.macAddress = macAddress;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public byte[] getMacAddress() {
        return macAddress;
    }

    public String getMacAddressAsString() {
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x", macAddress[0], macAddress[1], macAddress[2],
            macAddress[3], macAddress[4], macAddress[5]);
    }

}