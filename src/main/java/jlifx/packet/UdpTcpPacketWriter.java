package jlifx.packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;

public class UdpTcpPacketWriter implements PacketWriter {
    private static Socket tcpSocket;
    private static DatagramSocket udpSocket;
    private static DataOutputStream outputStream;
    private static InputStream inputStream;

    public List<Packet> sendPacketAndWaitForResponse(GatewayBulb gatewayBulb, Packet packet) //
        throws IOException {
        List<Packet> result = new ArrayList<Packet>();
        connect(gatewayBulb.getInetAddress());
        PacketReader packetReader;
        if (useTcp()) {
            packetReader = new TcpPacketReader(inputStream);
        } else {
            packetReader = new UdpPacketReader(udpSocket);
        }
        packetReader.start();
        sendPacket(gatewayBulb, packet);
        wait(500);
        sendPacket(gatewayBulb, packet);
        packetReader.sync();
        result = packetReader.getReceivedPackets();
        return result;
    }

    @Override
    public void sendPacket(GatewayBulb gatewayBulb, Packet packet) throws IOException {
        packet.setGatewayMac(gatewayBulb.getMacAddress());
        if (useTcp()) {
            outputStream.write(packet.toByteArray());
        } else {
            udpSocket.send(packet.toDatagramPacket(gatewayBulb.getInetAddress()));
        }
        wait(10);
    }

    public void connect(InetAddress address) throws IOException {
        try {
            connectTcp(address);
        } catch (ConnectException e) {
            connectUdp(address);
        }
    }

    private boolean useTcp() {
        return tcpSocket != null && tcpSocket.isConnected();
    }

    private void connectUdp(InetAddress address) throws IOException {
        if (udpSocket == null || !udpSocket.isConnected()) {
            udpSocket = new DatagramSocket(DiscoveryService.PORT);
            udpSocket.connect(address, DiscoveryService.PORT);
            udpSocket.setSoTimeout(1000);
            udpSocket.setReuseAddress(true);
        }
    }

    private void connectTcp(InetAddress address) throws IOException {
        if (tcpSocket == null || !tcpSocket.isConnected()) {
            tcpSocket = new Socket(address, DiscoveryService.PORT);
            tcpSocket.setSoTimeout(1000);
            tcpSocket.setReuseAddress(true);
            outputStream = new DataOutputStream(tcpSocket.getOutputStream());
            inputStream = tcpSocket.getInputStream();
        }
    }

    @Override
    public void close() throws IOException {
        if (tcpSocket != null && !tcpSocket.isClosed()) {
            tcpSocket.close();
        } else if (udpSocket != null && !udpSocket.isClosed()) {
            udpSocket.close();
        }
    }

    private void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e = null; /* do nothing */
        }
    }

}