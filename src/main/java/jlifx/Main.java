package jlifx;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
    private static final Log LOG = LogFactory.getLog(Main.class);
    private static final int PORT = 56700;
    private static PrintStream OUT = System.out;

    private static void printUsage() {
        OUT.println("Usage:");
        OUT.println("  java -jar lifx.jar <command>");
        OUT.println("");
        OUT.println("Where command can be:");
        OUT.println("  scan:");
        OUT.println("    Scan local network for a gateway bulb");
        OUT.println("  switch <mac-address> [on|off]:");
        OUT.println("    Switch selected bulb on/off");
        OUT.println("  color <mac-address> <rgb>:");
        OUT.println("    Set selected bulb to selected color (max. brightness)");
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }
        if (args[0].equalsIgnoreCase("scan")) {
            new Main().scan();
        } else if (args[0].equalsIgnoreCase("switch")) {
            if (args.length != 3 || (!(args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("off")))) {
                printUsage();
            } else {
                new Main().powerSwitchBulb(parseMacAddress(args[1]), args[2].equalsIgnoreCase("on") ? true : false);
            }
        } else if (args[0].equalsIgnoreCase("color")) {
            if (args.length != 3) {
                printUsage();
            } else {
                new Main().colorizeBulb(parseMacAddress(args[1]), Color.decode("#" + args[2]));
            }
        }
    }

    private static byte[] parseMacAddress(String macAddress) {
        byte[] result = new byte[6];
        result[0] = (byte)(Integer.parseInt(macAddress.substring(0, 2), 16));
        result[1] = (byte)(Integer.parseInt(macAddress.substring(3, 5), 16));
        result[2] = (byte)(Integer.parseInt(macAddress.substring(6, 8), 16));
        result[3] = (byte)(Integer.parseInt(macAddress.substring(9, 11), 16));
        result[4] = (byte)(Integer.parseInt(macAddress.substring(12, 14), 16));
        result[5] = (byte)(Integer.parseInt(macAddress.substring(15, 17), 16));
        return result;
    }

    private void scan() {
        Bulb gatewayBulb = discoverGatewayBulb();
        if (gatewayBulb == null) {
            OUT.println("No LifX gateway bulb found!");
        } else {
            OUT.println("Found LifX gateway bulb!");
            OUT.println("  IP address: " + gatewayBulb.getInetAddress());
            OUT.println("  Mac address: " + gatewayBulb.getMacAddressAsString());
        }
    }

    private void powerSwitchBulb(byte[] macAddress, boolean on) throws IOException {
        Bulb gatewayBulb = discoverGatewayBulb();
        PowerManagementPacket packet = new PowerManagementPacket(macAddress, on);
        Socket socket = new Socket(gatewayBulb.getInetAddress(), PORT);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.write(packet.toByteArray());
        socket.close();
    }
    
    private void colorizeBulb(byte[] macAddress, Color color) throws IOException {
        Bulb gatewayBulb = discoverGatewayBulb();
        Packet packet = new ColorManagementPacket(macAddress, color);
        Socket socket = new Socket(gatewayBulb.getInetAddress(), PORT);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.write(packet.toByteArray());
        socket.close();
    }

    private Bulb discoverGatewayBulb() {
        LOG.info("Discovering gateway bulb...");
        List<InetAddress> networkBroadcastAddresses = getNetworkBroadcastAddresses();
        for (InetAddress broadcastAddress : networkBroadcastAddresses) {
            Bulb result = discoverGatewayBulb(broadcastAddress);
            if (result != null) {
                LOG.info("Found gateway bulb! (" + result.getInetAddress() + ")");
                return result;
            }
        }
        return null;
    }

    private Bulb discoverGatewayBulb(InetAddress broadcastAddress) {
        Bulb result = null;
        Packet packet = new Packet();
        byte[] byteArray = packet.toByteArray();
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            DatagramPacket datagramPacket = new DatagramPacket(byteArray, 0, byteArray.length, broadcastAddress, PORT);
            socket.send(datagramPacket);
            DatagramPacket answer = new DatagramPacket(byteArray, 0, byteArray.length);
            socket.receive(answer);
            if (answer.getAddress().isSiteLocalAddress()) {
                socket.receive(answer);
            }
            Packet answerLifxPacket = Packet.fromDatagramPacket(answer);
            result = new Bulb(answer.getAddress(), answerLifxPacket.getGatewayMac());
        } catch (SocketException e) {} catch (IOException e) {}
        return result;
    }

    List<InetAddress> getNetworkBroadcastAddresses() {
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
