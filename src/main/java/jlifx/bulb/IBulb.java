package jlifx.bulb;

import jlifx.packet.MacAddress;
import jlifx.packet.StatusResponsePacket;

import java.awt.*;
import java.io.IOException;

public interface IBulb {
    MacAddress getMacAddress();

    String getMacAddressAsString();

    GatewayBulb getGatewayBulb();

    void switchOn() throws IOException;

    void switchOff() throws IOException;

    void colorize(Color color, int fadetime, float brightness) throws IOException;

    StatusResponsePacket getStatus();

    void setStatus(StatusResponsePacket status);

    String getName();

    int getHue();

    int getSaturation();

    int getBrightness();

    int getKelvin();

    int getDim();

    void setDim(float brightness) throws IOException;

    int getPower();

    BulbMeshFirmwareStatus getMeshFirmwareStatus() throws IOException;
}
