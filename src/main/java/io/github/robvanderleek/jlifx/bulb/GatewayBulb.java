package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.common.MacAddress;
import io.github.robvanderleek.jlifx.packet.Packet;
import io.github.robvanderleek.jlifx.packet.PacketReader;
import io.github.robvanderleek.jlifx.packet.PacketService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

public class GatewayBulb extends Bulb {
    private static final Log LOG = LogFactory.getLog(GatewayBulb.class);
    private static final int MAX_NR_RETRIES = 3;
    private final InetAddress address;
    private DatagramSocket socket;
    private PacketReader packetReader;
    private PacketService packetService = new PacketService();
    private byte packetSequenceNumber = 0x00;

    public GatewayBulb(InetAddress address, MacAddress macAddress) {
        super(macAddress);
        this.address = address;
        setGatewayBulb(this);
    }

    public InetAddress getInetAddress() {
        return address;
    }

    public PacketService getPacketService() {
        return packetService;
    }

    private void connect() throws IOException {
        if (socket == null || !socket.isConnected()) {
            socket = new DatagramSocket();
            socket.connect(address, BulbDiscoveryService.getGatewayDiscoveryPort());
            socket.setSoTimeout(0);
            socket.setReuseAddress(true);
            packetReader = new PacketReader(socket);
            packetReader.start();
            packetReader.waitForStartup();
        }
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            packetReader.stopRunning();
        }
    }

    private byte getNextPacketSequenceNumber() {
        return packetSequenceNumber++;
    }

    public void sendPacket(Packet packet) throws IOException {
        connect();
        packet.setAckRequired(true);
        byte sequenceNumber = getNextPacketSequenceNumber();
        packet.setSequenceNumber(sequenceNumber);
        for (int i = 0; i < MAX_NR_RETRIES; i++) {
            sendPacketFireAndForget(packet);
            waitForNetworkLag();
            if (packetReader.hasAcknowledgement(sequenceNumber)) {
                return;
            }
        }
        LOG.error("Did not receive acknowledgement packet from bulb");
    }

    public Packet sendPacketAndGetResponse(Packet packet) throws IOException {
        connect();
        byte sequenceNumber = getNextPacketSequenceNumber();
        packet.setResponseRequired(true);
        packet.setSequenceNumber(sequenceNumber);
        for (int i = 0; i < MAX_NR_RETRIES; i++) {
            sendPacketFireAndForget(packet);
            waitForNetworkLag();
            Optional<Packet> responsePacket = packetReader.getResponsePacket(sequenceNumber, packet.getResponseType());
            if (responsePacket.isPresent()) {
                return responsePacket.get();
            }
        }
        LOG.error("Did not receive response packet from bulb");
        return null;
    }

    public List<Packet> sendPacketAndGetResponses(Packet packet) throws IOException {
        connect();
        byte sequenceNumber = getNextPacketSequenceNumber();
        packet.setResponseRequired(true);
        packet.setSequenceNumber(sequenceNumber);
        for (int i = 0; i < MAX_NR_RETRIES; i++) {
            sendPacketFireAndForget(packet);
            waitForNetworkLag();
        }
        return packetReader.getResponsePackets(packet.getResponseType());
    }

    private void sendPacketFireAndForget(Packet packet) throws IOException {
        socket.send(packet.toDatagramPacket(address, BulbDiscoveryService.getGatewayDiscoveryPort()));
    }

    private void waitForNetworkLag() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }
}