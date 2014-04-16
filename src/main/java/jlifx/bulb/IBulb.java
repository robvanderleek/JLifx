package jlifx.bulb;

import java.awt.Color;
import java.io.IOException;

import jlifx.packet.StatusResponsePacket;

public interface IBulb {
    byte[] getMacAddress();

    String getMacAddressAsString();

    GatewayBulb getGatewayBulb();

    void switchOn() throws IOException;

    void switchOff() throws IOException;

    void colorize(Color color, int fadetime) throws IOException;

    StatusResponsePacket getStatus();

    void setStatus(StatusResponsePacket status);

    String getName();

    int getHue();

    int getSaturation();

    int getBrightness();

    int getKelvin();

    int getDim();

    int getPower();
}