package jlifx.packet;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import jlifx.bulb.Bulb;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;

public class PacketService {
    private static Socket socket;
    private static DataOutputStream outputStream;

    public static void sendPowerManagementPacket(Bulb bulb, boolean on) throws IOException {
        Packet packet = new PowerManagementPacket(bulb.getMacAddress(), on);
        sendPacket(bulb.getGatewayBulb(), packet);
    }

    public static List<Packet> sendStatusRequestPacket(GatewayBulb bulb) throws IOException {
        Packet packet = new StatusRequestPacket();
        return sendPacketAndWaitForResponse(bulb.getGatewayBulb(), packet);
    }

    public static void sendColorManagementPacket(Bulb bulb, Color color, int fadetime) throws IOException {
        Packet packet = new ColorManagementPacket(bulb.getMacAddress(), color, fadetime);
        sendPacket(bulb.getGatewayBulb(), packet);
    }

    private static void sendPacket(GatewayBulb bulb, Packet packet) throws IOException {
        packet.setGatewayMac(bulb.getMacAddress());
        if (socket == null || !socket.isConnected()) {
            socket = new Socket(bulb.getInetAddress(), DiscoveryService.PORT);
            socket.setReuseAddress(true);
            outputStream = new DataOutputStream(socket.getOutputStream());
        }
        outputStream.write(packet.toByteArray());
    }

    private static List<Packet> sendPacketAndWaitForResponse(GatewayBulb bulb, Packet packet) throws IOException {
        List<Packet> result = new ArrayList<Packet>();
        packet.setGatewayMac(bulb.getMacAddress());
        Socket socket = new Socket(bulb.getInetAddress(), DiscoveryService.PORT);
        socket.setSoTimeout(1000);
        socket.getOutputStream().write(packet.toByteArray());
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[128];
        try {
            while (true) {
                int length = inputStream.read(buffer);
                if (length > 0) {
                    result.add(Packet.fromByteArray(buffer));
                }
            }
        } catch (SocketTimeoutException e) {

        }
        socket.close();
        return result;
    }

    public static void closeSocket() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

}
