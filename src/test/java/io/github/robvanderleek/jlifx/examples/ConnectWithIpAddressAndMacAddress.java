package io.github.robvanderleek.jlifx.examples;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.commandline.Utils;
import io.github.robvanderleek.jlifx.common.Color;

import java.io.IOException;
import java.net.InetAddress;

public class ConnectWithIpAddressAndMacAddress {

    public static void main(String[] args) throws IOException {
        Bulb bulb = new Bulb(InetAddress.getByName("192.168.178.50"), Utils.parseMacAddress("aa:bb:cc:dd:ee:ff"));
        bulb.switchOff();
        bulb.switchOn();
        int power = bulb.getPower();
        if (power == 0xFFFF) {
            bulb.colorize(Color.GREEN);
        }
        bulb.disconnect();
    }

}