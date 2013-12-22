package jlifx.bulb;

import java.awt.Color;
import java.io.IOException;

import jlifx.packet.PacketService;

public class Bulb {
    private final byte[] macAddress;
    private final GatewayBulb gatewayBulb;
    private String name;

    public Bulb(byte[] macAddress, GatewayBulb gatewayBulb) {
        this.macAddress = macAddress;
        this.gatewayBulb = gatewayBulb;
    }

    Bulb(byte[] macAddress) {
        this.macAddress = macAddress;
        this.gatewayBulb = null;
    }

    public byte[] getMacAddress() {
        return macAddress;
    }

    public GatewayBulb getGatewayBulb() {
        return gatewayBulb;
    }

    public String getMacAddressAsString() {
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x", macAddress[0], macAddress[1], macAddress[2],
            macAddress[3], macAddress[4], macAddress[5]);
    }

    public void switchOn() throws IOException {
        PacketService.sendPowerManagementPacket(this, true);
    }

    public void switchOff() throws IOException {
        PacketService.sendPowerManagementPacket(this, false);
    }

    public void colorize(Color color) throws IOException {
        PacketService.sendColorManagementPacket(this, color);
    }

    public String getName() {
        return name;
    }

    public void setName(String bulbName) {
        name = bulbName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Bulb))
            return false;

        Bulb that = (Bulb)obj;

        return that.macAddress[0] == macAddress[0] && that.macAddress[1] == macAddress[1]
            && that.macAddress[2] == macAddress[2] && that.macAddress[3] == macAddress[3]
            && that.macAddress[4] == macAddress[4] && that.macAddress[5] == macAddress[5];
    }

    @Override
    public int hashCode() {
        return macAddress[0] + macAddress[1] + macAddress[2] + macAddress[3] + macAddress[4] + macAddress[5];
    }

}