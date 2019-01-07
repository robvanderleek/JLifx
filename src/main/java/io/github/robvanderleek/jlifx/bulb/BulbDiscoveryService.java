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
     * Returns the first valid Gateway bulb discovered, or null if no gateway bulb was
     * discovered in any of the networks.
     *
     * @return First Gateway bulb discovered
     */
    public static GatewayBulb discoverGatewayBulb() {
        List<InetAddress> networkBroadcastAddresses = getNetworkBroadcastAddresses();
        for (InetAddress broadcastAddress : networkBroadcastAddresses) {
            try {
                GatewayBulb result = discoverGatewayBulbOnNetwork(broadcastAddress);
                if (result != null) {
                    return result;
                }
            } catch (IOException e) {
                LOG.error(e);
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * Returns the bulb with the given name if it can be discovered, empty otherwise.
     *
     * @return Bulb with given name or empty
     */
    public static Optional<Bulb> discoverBulbByName(GatewayBulb gatewayBulb, String name) {
        return discoverAllBulbs(gatewayBulb).stream().filter(b -> b.getName().equalsIgnoreCase(name)).findAny();
    }

    private static GatewayBulb discoverGatewayBulbOnNetwork(InetAddress broadcastAddress) throws IOException {
        GatewayBulb result = null;
        Packet packet = new Packet();
        byte[] byteArray = packet.toByteArray();
        DatagramSocket socket = new DatagramSocket();
        socket.setReuseAddress(true);
        DatagramPacket datagramPacket = new DatagramPacket(byteArray, byteArray.length, broadcastAddress,
                gatewayDiscoveryPort);
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
                return new GatewayBulb(answer.getAddress(), packet.getTargetMac());
            }
        }
        return result;
    }

    private static boolean isAnswerFromGatewayBulb(DatagramPacket packet) throws SocketException {
        if (!ignoreGatewaysOnLocalhost) return true;
        InetAddress inetAddress = packet.getAddress();
        if (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress() ||
                NetworkInterface.getByInetAddress(inetAddress) != null) {
            return false;
        } else {
            return true;
        }
    }

    public static Set<Bulb> discoverAllBulbs(GatewayBulb gatewayBulb) {
        Set<Bulb> result = new HashSet<>();
        List<StatusResponsePacket> packets = new PacketService().sendStatusRequestPacket(gatewayBulb);
        for (StatusResponsePacket packet : packets) {
            if (packet.getType() == 0x6B) {
                Bulb bulb = new Bulb(packet.getTargetMac(), gatewayBulb);
                bulb.setStatus(packet);
                result.add(bulb);
            }
        }
        return result;
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