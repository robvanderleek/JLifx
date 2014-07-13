package jlifx.packet;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import jlifx.bulb.BulbMeshFirmwareStatus;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;
import jlifx.bulb.IBulb;

public final class PacketService {
    private static Socket tcpSocket;
    private static DatagramSocket udpSocket;
    private static DataOutputStream outputStream;
    private static InputStream inputStream;

    private PacketService() {}

    public static void sendPowerManagementPacket(IBulb bulb, boolean on) throws IOException {
        Packet packet = new PowerManagementPacket(bulb.getMacAddress(), on);
        connect(bulb.getGatewayBulb().getInetAddress());
        sendPacket(bulb.getGatewayBulb(), packet);
    }

    public static List<Packet> sendStatusRequestPacket(GatewayBulb bulb) throws IOException {
        Packet packet = new StatusRequestPacket();
        return sendPacketAndWaitForResponse(bulb, packet);
    }

    public static List<Packet> sendWifiInfoRequestPacket(GatewayBulb bulb) throws IOException {
        Packet packet = new WifiInfoRequestPacket();
        return sendPacketAndWaitForResponse(bulb, packet);
    }

    public static void sendColorManagementPacket(IBulb bulb, Color color, int fadetime, float brightness)
        throws IOException {
        Packet packet = new ColorManagementPacket(bulb.getMacAddress(), color, fadetime, brightness);
        connect(bulb.getGatewayBulb().getInetAddress());
        sendPacket(bulb.getGatewayBulb(), packet);
    }

    public static BulbMeshFirmwareStatus getMeshFirmwareStatus(GatewayBulb bulb) throws IOException {
        Packet packet = new MeshFirmwareRequestPacket();
        List<Packet> responsePackets = sendPacketAndWaitForResponse(bulb, packet);
        if (!responsePackets.isEmpty()) {
            return BulbMeshFirmwareStatus.fromPacket(responsePackets.get(0));
        } else {
            return null;
        }
    }

    public static void sendSetDimAbsolutePacket(IBulb bulb, float brightness) throws IOException {
        Packet packet = new SetDimAbsolutePacket(brightness);
        connect(bulb.getGatewayBulb().getInetAddress());
        sendPacket(bulb.getGatewayBulb(), packet);
    }

    public static List<Packet> sendPacketAndWaitForResponse(GatewayBulb gatewayBulb, Packet packet) //
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

    private static void sendPacket(GatewayBulb gatewayBulb, Packet packet) throws IOException {
        packet.setGatewayMac(gatewayBulb.getMacAddress());
        if (useTcp()) {
            outputStream.write(packet.toByteArray());
        } else {
            udpSocket.send(packet.toDatagramPacket(gatewayBulb.getInetAddress()));
        }
        wait(10);
    }

    private static void connect(InetAddress address) throws IOException {
        try {
            connectTcp(address);
        } catch (ConnectException e) {
            connectUdp(address);
        }
    }

    private static boolean useTcp() {
        return tcpSocket != null && tcpSocket.isConnected();
    }

    private static void connectUdp(InetAddress address) throws IOException {
        if (udpSocket == null || !udpSocket.isConnected()) {
            udpSocket = new DatagramSocket(DiscoveryService.PORT);
            udpSocket.connect(address, DiscoveryService.PORT);
            udpSocket.setSoTimeout(1000);
            udpSocket.setReuseAddress(true);
        }
    }

    private static void connectTcp(InetAddress address) throws IOException {
        if (tcpSocket == null || !tcpSocket.isConnected()) {
            tcpSocket = new Socket(address, DiscoveryService.PORT);
            tcpSocket.setSoTimeout(1000);
            tcpSocket.setReuseAddress(true);
            outputStream = new DataOutputStream(tcpSocket.getOutputStream());
            inputStream = tcpSocket.getInputStream();
        }
    }

    public static void closeSocket() throws IOException {
        if (tcpSocket != null && !tcpSocket.isClosed()) {
            tcpSocket.close();
        } else if (udpSocket != null && !udpSocket.isClosed()) {
            udpSocket.close();
        }
    }

    private static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e = null; /* do nothing */
        }
    }

}