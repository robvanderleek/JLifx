package jlifx.bulb;

import jlifx.packet.MacAddress;
import jlifx.packet.StatusResponsePacket;

import java.awt.*;
import java.io.IOException;

public class Bulb implements IBulb {
    private final MacAddress macAddress;
    private GatewayBulb gatewayBulb;
    private StatusResponsePacket status;
    private BulbMeshFirmwareStatus meshFirmwareStatus;

    public Bulb(MacAddress macAddress, GatewayBulb gatewayBulb) {
        this.macAddress = macAddress;
        this.gatewayBulb = gatewayBulb;
    }

    Bulb(MacAddress macAddress) {
        this.macAddress = macAddress;
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }

    public GatewayBulb getGatewayBulb() {
        return gatewayBulb;
    }

    public void setGatewayBulb(GatewayBulb gatewayBulb) {
        this.gatewayBulb = gatewayBulb;
    }

    public String getMacAddressAsString() {
        return macAddress.toString();
    }

    public void switchOn() throws IOException {
        gatewayBulb.getPacketService().sendPowerManagementPacket(this, true);
    }

    public void switchOff() throws IOException {
        gatewayBulb.getPacketService().sendPowerManagementPacket(this, false);
    }

    public void colorize(Color color, int fadetime, float brightness) throws IOException {
        gatewayBulb.getPacketService().sendColorManagementPacket(this, color, fadetime, brightness);
    }

    public StatusResponsePacket getStatus() {
        return status;
    }

    public void setStatus(StatusResponsePacket status) {
        this.status = status;
    }

    public String getName() {
        return getStatus().getBulbName();
    }

    public int getHue() {
        return getStatus().getHue();
    }

    public int getSaturation() {
        return getStatus().getSaturation();
    }

    public int getBrightness() {
        return getStatus().getBrightness();
    }

    public int getKelvin() {
        return getStatus().getKelvin();
    }

    public int getDim() {
        return getStatus().getDim();
    }

    public void setDim(float brightness) throws IOException {
        gatewayBulb.getPacketService().sendSetDimAbsolutePacket(this, brightness);
    }

    public int getPower() {
        return getStatus().getPower();
    }

    public BulbMeshFirmwareStatus getMeshFirmwareStatus() throws IOException {
        if (meshFirmwareStatus == null) {
            meshFirmwareStatus = gatewayBulb.getPacketService().getMeshFirmwareStatus(this.getGatewayBulb());
        }
        return meshFirmwareStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Bulb)) return false;

        Bulb that = (Bulb) obj;

        return that.macAddress.equals(this.macAddress);
    }

    @Override
    public int hashCode() {
        return macAddress.hashCode();
    }

}
