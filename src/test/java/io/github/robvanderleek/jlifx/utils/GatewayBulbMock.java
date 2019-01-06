package io.github.robvanderleek.jlifx.utils;

import io.github.robvanderleek.jlifx.packet.StatusResponsePacket;
import io.github.robvanderleek.jlifx.packet.StatusResponsePacketTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase.TEST_MAC_ADDRESS_1;

public class GatewayBulbMock {
    private final DatagramSocket socket;
    private boolean keepRunning = false;

    public GatewayBulbMock(int port) throws SocketException {
        socket = new DatagramSocket(port);
        socket.setSoTimeout(0);
        socket.setReuseAddress(true);
    }

    public void run() {
        keepRunning = true;
        new Thread(() -> {
            while (keepRunning) {
                try {
                    byte[] buffer = new byte[128];
                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(datagramPacket);
                    StatusResponsePacket packet = StatusResponsePacketTest.makeTestPacket();
                    packet.setTargetMac(TEST_MAC_ADDRESS_1);
                    socket.send(packet.toDatagramPacket(datagramPacket.getAddress(), datagramPacket.getPort()));
                } catch (IOException e) {
                    keepRunning = false;
                }
            }
        }).start();
    }

    public void stop() {
        keepRunning = false;
        socket.close();
    }
}
