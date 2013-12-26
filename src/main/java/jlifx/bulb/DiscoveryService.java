package jlifx.bulb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jlifx.packet.Packet;
import jlifx.packet.PacketService;
import jlifx.packet.StatusResponsePacket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DiscoveryService {
    public static final int PORT = 56700;
    private static final Log LOG = LogFactory.getLog(DiscoveryService.class);

    private DiscoveryService() {}

    public static GatewayBulb discoverGatewayBulb() throws IOException {
        List<InetAddress> networkBroadcastAddresses = getNetworkBroadcastAddresses();
        for (InetAddress broadcastAddress : networkBroadcastAddresses) {
            GatewayBulb result = discoverGatewayBulbOnNetwork(broadcastAddress);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static GatewayBulb discoverGatewayBulbOnNetwork(InetAddress broadcastAddress) throws IOException {
        GatewayBulb result = null;
        Packet packet = new Packet();
        byte[] byteArray = packet.toByteArray();
        DatagramSocket socket = new DatagramSocket(PORT);
        socket.setReuseAddress(true);
        DatagramPacket datagramPacket = new DatagramPacket(byteArray, byteArray.length, broadcastAddress, PORT);
        int retries = 3;
        while (result == null && retries > 0) {
            socket.send(datagramPacket);
            result = waitForReply(socket);
            retries--;
        }
        socket.close();
        return result;
    }

    private static GatewayBulb waitForReply(DatagramSocket socket) throws IOException {
        GatewayBulb result = null;
        socket.setSoTimeout(500);
        int retries = 3;
        byte[] byteArray = new byte[128];
        while (retries > 0) {
            DatagramPacket answer = new DatagramPacket(byteArray, byteArray.length);
            try {
                socket.receive(answer);
            } catch (SocketTimeoutException e) {
                retries--;
                continue;
            }
            if (isAnswerFromGatewayBulb(answer)) {
                return new GatewayBulb(answer.getAddress(), Packet.fromDatagramPacket(answer).getGatewayMac());
            }
            retries--;
        }
        return result;
    }

    private static boolean isAnswerFromGatewayBulb(DatagramPacket packet) throws SocketException {
        InetAddress inetAddress = packet.getAddress();
        if (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress()
            || NetworkInterface.getByInetAddress(inetAddress) != null) {
            return false;
        } else {
            return true;
        }
    }

    public static Collection<Bulb> discoverAllBulbs(GatewayBulb gatewayBulb) throws IOException {
        Set<Bulb> result = new HashSet<Bulb>();
        List<Packet> packets = PacketService.sendStatusRequestPacket(gatewayBulb);
        for (Packet packet : packets) {
            StatusResponsePacket responsePacket = new StatusResponsePacket(packet);
            Bulb bulb = new Bulb(responsePacket.getTargetMac(), gatewayBulb);
            bulb.setStatus(responsePacket);
            result.add(bulb);
        }
        return result;
    }

    public static Bulb lookupBulb(byte[] macAddress) throws IOException {
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        return new Bulb(macAddress, gatewayBulb);
    }

    static List<InetAddress> getNetworkBroadcastAddresses() {
        List<InetAddress> result = new ArrayList<InetAddress>();
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        InetAddress broadcast = interfaceAddress.getBroadcast();
                        if (broadcast != null) {
                            result.add(broadcast);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            LOG.error("Could not retreive broadcast addresses from available network interfaces");
        }
        return result;
    }
}