package jlifx.packet;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import jlifx.bulb.Bulb;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;

public final class PacketService {
    private static Socket socket;
    private static DataOutputStream outputStream;
    private static InputStream inputStream;

    private PacketService() {}

    public static void sendPowerManagementPacket(Bulb bulb, boolean on) throws IOException {
        Packet packet = new PowerManagementPacket(bulb.getMacAddress(), on);
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

    public static void sendColorManagementPacket(Bulb bulb, Color color, int fadetime) throws IOException {
        Packet packet = new ColorManagementPacket(bulb.getMacAddress(), color, fadetime);
        sendPacket(bulb.getGatewayBulb(), packet);
    }

    private static void sendPacket(GatewayBulb bulb, Packet packet) throws IOException {
        packet.setGatewayMac(bulb.getMacAddress());
        connect(bulb.getInetAddress());
        outputStream.write(packet.toByteArray());
    }

    public static List<Packet> sendPacketAndWaitForResponse(GatewayBulb gatewayBulb, Packet packet) //
        throws IOException {
        List<Packet> result = new ArrayList<Packet>();
        packet.setGatewayMac(gatewayBulb.getMacAddress());
        connect(gatewayBulb.getInetAddress());
        outputStream.write(packet.toByteArray()); // Dummy request to warm up gateway buble
        wait(500);
        PacketReaderThread packetReaderThread = new PacketReaderThread(inputStream);
        packetReaderThread.start();
        outputStream.write(packet.toByteArray());
        packetReaderThread.sync();
        result = packetReaderThread.getReceivedPackets();
        return result;
    }

    private static void connect(InetAddress address) throws IOException {
        if (socket == null || !socket.isConnected()) {
            socket = new Socket(address, DiscoveryService.PORT);
            socket.setSoTimeout(1000);
            socket.setReuseAddress(true);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = socket.getInputStream();
        }
    }

    public static void closeSocket() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    private static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            /* do nothing */
        }
    }

    private static class PacketReaderThread extends Thread implements Runnable {
        private InputStream inputStream;
        private List<Packet> receivedPackets = new ArrayList<Packet>();
        private Object monitor = new Object();

        public PacketReaderThread(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public void sync() {
            try {
                synchronized (monitor) {
                    monitor.wait();
                }
            } catch (InterruptedException e) {
                /* do nothing */
            }
        }

        @Override
        public void run() {
            try {
                byte[] buffer = new byte[128];
                while (true) {
                    int length = inputStream.read(buffer);
                    if (length > 0) {
                        receivedPackets.add(Packet.fromByteArray(buffer));
                    }
                }
            } catch (SocketTimeoutException e) {
                /* do nothing */
            } catch (IOException e) {
                /* do nothing */
            }
            synchronized (monitor) {
                monitor.notify();
            }
        }

        public List<Packet> getReceivedPackets() {
            return receivedPackets;
        }

    }

}
