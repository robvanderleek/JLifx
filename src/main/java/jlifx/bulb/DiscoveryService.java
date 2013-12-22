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
import java.util.Collections;
import java.util.List;

import jlifx.packet.Packet;
import jlifx.packet.PacketService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DiscoveryService {
    public static final int PORT = 56700;
    private static final Log LOG = LogFactory.getLog(DiscoveryService.class);

    public static GatewayBulb discoverGatewayBulb() {
        List<InetAddress> networkBroadcastAddresses = getNetworkBroadcastAddresses();
        for (InetAddress broadcastAddress : networkBroadcastAddresses) {
            GatewayBulb result = discoverGatewayBulbOnNetwork(broadcastAddress);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static GatewayBulb discoverGatewayBulbOnNetwork(InetAddress broadcastAddress) {
        GatewayBulb result = null;
        Packet packet = new Packet();
        byte[] byteArray = packet.toByteArray();
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            socket.setReuseAddress(true);
            DatagramPacket datagramPacket = new DatagramPacket(byteArray, 0, byteArray.length, broadcastAddress, PORT);
            socket.setSoTimeout(1000);
            boolean gatewayResponse = false;
            int retries = 3;
            socket.send(datagramPacket);
            while (!gatewayResponse && retries > 0) {
                DatagramPacket answer = new DatagramPacket(byteArray, byteArray.length);
                try {
                    socket.receive(answer);
                } catch (SocketTimeoutException e) {
                    retries--;
                    continue;
                }
                InetAddress inetAddress = answer.getAddress();
                if (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress()
                    || NetworkInterface.getByInetAddress(inetAddress) != null) {
                    retries--;
                    continue;
                } else {
                    gatewayResponse = true;
                    Packet answerLifxPacket = Packet.fromDatagramPacket(answer);
                    result = new GatewayBulb(answer.getAddress(), answerLifxPacket.getGatewayMac());
                }
            }
            socket.close();
        } catch (SocketException e) {} catch (IOException e) {}

        return result;
    }

    public static List<Bulb> discoverAllBulbs(GatewayBulb gatewayBulb) throws IOException {
        List<Bulb> result = new ArrayList<Bulb>();
        List<Packet> sendStatusRequestPacket = PacketService.sendStatusRequestPacket(gatewayBulb);
        for (Packet packet : sendStatusRequestPacket) {
            result.add(new Bulb(packet.getTargetMac(), gatewayBulb));
        }
        return result;
    }

    public static Bulb lookupBulb(byte[] macAddress) {
        return new Bulb(macAddress, null);
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