package jlifx.bulb;

import java.awt.Color;
import java.io.IOException;

import jlifx.commandline.Utils;
import jlifx.packet.StatusResponsePacket;

public class Bulb implements IBulb {
    private final byte[] macAddress;
    private final GatewayBulb gatewayBulb;
    private StatusResponsePacket status;
    private BulbMeshFirmwareStatus meshFirmwareStatus;

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
        return Utils.getMacAddressAsString(macAddress);
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
