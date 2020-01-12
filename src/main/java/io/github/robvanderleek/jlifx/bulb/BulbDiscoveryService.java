package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.packet.Packet;
import io.github.robvanderleek.jlifx.packet.PacketService;
import io.github.robvanderleek.jlifx.packet.StatusResponsePacket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class BulbDiscoveryService {
    private static final Log LOG = LogFactory.getLog(BulbDiscoveryService.class);
    private static int gatewayDiscoveryPort = 56700;
    private static boolean ignoreGatewaysOnLocalhost = true;

    private BulbDiscoveryService() {
    }

    /**
     * Returns Gateway bulbs discovered in any of the networks.
     */
    public static List<Bulb> discoverBulbs() {
        List<Bulb> result = new ArrayList<>();
        List<InetAddress> networkBroadcastAddresses = getNetworkBroadcastAddresses();
        for (InetAddress broadcastAddress : networkBroadcastAddresses) {
            try {
                Set<Bulb> bulbs = discoverBulbsOnNetwork(broadcastAddress);
                for (Bulb bulb : bulbs) {
                    List<StatusResponsePacket> packets = new PacketService().sendStatusRequestPacket(bulb);
                    for (StatusResponsePacket packet : packets) {
                        if (packet.getType() == 0x6B) {
                            bulb.setStatus(packet);
                        }
                    }
                    bulb.disconnect();
                }
                result.addAll(bulbs);
            } catch (IOException e) {
                LOG.error(e);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * Returns the bulb with the given name if it can be discovered, empty otherwise.
     */
    public static Optional<Bulb> discoverBulbByName(String name) {
        return discoverBulbs().stream().filter(b -> b.getName().equalsIgnoreCase(name)).findAny();
    }

    /**
     * Returns the bulb with the given IP address if it can be discovered, empty otherwise.
     */
    public static Optional<Bulb> discoverBulbByIpAddress(String ipAddress) {
        return discoverBulbs().stream().filter(b -> b.getIpAddress().equalsIgnoreCase(ipAddress)).findAny();
    }

    private static Set<Bulb> discoverBulbsOnNetwork(InetAddress broadcastAddress) throws IOException {
        Set<Bulb> result = new HashSet<>();
        Packet packet = new Packet();
        byte[] byteArray = packet.toByteArray();
        DatagramSocket socket = new DatagramSocket();
        socket.setReuseAddress(true);
        DatagramPacket datagramPacket =
                new DatagramPacket(byteArray, byteArray.length, broadcastAddress, gatewayDiscoveryPort);
        int retries = 3;
        while (retries > 0) {
            socket.send(datagramPacket);
            result.addAll(waitForReply(socket));
            retries--;
        }
        socket.close();
        return result;
    }

    private static Set<Bulb> waitForReply(DatagramSocket socket) throws IOException {
        Set<Bulb> result = new HashSet<>();
        socket.setSoTimeout(500);
        int retries = 3;
        byte[] byteArray = new byte[128];
        while (retries-- > 0) {
            DatagramPacket answer = new DatagramPacket(byteArray, byteArray.length);
            try {
                socket.receive(answer);
            } catch (SocketTimeoutException e) {
                retries--;
                continue;
            }
            if (isAnswerFromGatewayBulb(answer)) {
                Packet packet = Packet.fromDatagramPacket(answer);
                result.add(new Bulb(answer.getAddress(), packet.getTargetMac()));
            }
        }
        return result;
    }

    private static boolean isAnswerFromGatewayBulb(DatagramPacket packet) throws SocketException {
        if (!ignoreGatewaysOnLocalhost)
            return true;
        InetAddress inetAddress = packet.getAddress();
        return !inetAddress.isAnyLocalAddress() && !inetAddress.isLoopbackAddress() &&
                NetworkInterface.getByInetAddress(inetAddress) == null;
    }

    static List<InetAddress> getNetworkBroadcastAddresses() {
        List<InetAddress> result = new ArrayList<>();
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

    public static int getGatewayDiscoveryPort() {
        return gatewayDiscoveryPort;
    }

    static void setGatewayDiscoveryPort(int port) {
        gatewayDiscoveryPort = port;
    }

    static void setIgnoreGatewaysOnLocalhost(boolean ignore) {
        ignoreGatewaysOnLocalhost = ignore;
    }

}
