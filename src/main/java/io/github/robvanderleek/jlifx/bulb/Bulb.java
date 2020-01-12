package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.commandline.Utils;
import io.github.robvanderleek.jlifx.common.Color;
import io.github.robvanderleek.jlifx.common.MacAddress;
import io.github.robvanderleek.jlifx.packet.Packet;
import io.github.robvanderleek.jlifx.packet.PacketReader;
import io.github.robvanderleek.jlifx.packet.PacketService;
import io.github.robvanderleek.jlifx.packet.StatusResponsePacket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

public class Bulb {
    private static final Log LOG = LogFactory.getLog(Bulb.class);
    private static final int MAX_NR_RETRIES = 3;
    private final InetAddress address;
    private DatagramSocket socket;
    private PacketReader packetReader;
    private PacketService packetService = new PacketService();
    private byte packetSequenceNumber = 0x00;
    private final MacAddress macAddress;
    private StatusResponsePacket status;
    private BulbMeshFirmwareStatus meshFirmwareStatus;

    public Bulb(InetAddress address, MacAddress macAddress) {
        this.address = address;
        this.macAddress = macAddress;
    }

    public PacketService getPacketService() {
        return packetService;
    }

    public void setPacketService(PacketService packetService) {
        this.packetService = packetService;
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }

    public String getMacAddressAsString() {
        return macAddress.toString();
    }

    public String getIpAddress() {
        return Utils.getIpAddressAsString(address.getAddress());
    }

    public void switchOn() throws IOException {
        getPacketService().sendPowerManagementPacket(this, true);
    }

    public void switchOff() throws IOException {
        getPacketService().sendPowerManagementPacket(this, false);
    }

    public void colorize(Color color, int fadetime, float brightness) {
        getPacketService().sendColorManagementPacket(this, color, fadetime, brightness);
    }

    private StatusResponsePacket getStatus() {
        return status;
    }

    void setStatus(StatusResponsePacket status) {
        this.status = status;
    }

    public String getName() {
        return getStatus().getBulbName();
    }

    public int getHue() {
        return getStatus().getHue();
    }

    public int getSaturation() {
        return getStatus().getSaturation();
    }

    public int getBrightness() {
        return getStatus().getBrightness();
    }

    public int getKelvin() {
        return getStatus().getKelvin();
    }

    public int getDim() {
        return getStatus().getDim();
    }

    public void setDim(float brightness) throws IOException {
        getPacketService().sendSetDimAbsolutePacket(this, brightness);
    }

    public int getPower() {
        return getStatus().getPower();
    }

    public BulbMeshFirmwareStatus getMeshFirmwareStatus() throws IOException {
        if (meshFirmwareStatus == null) {
            meshFirmwareStatus = getPacketService().getMeshFirmwareStatus(this);
        }
        return meshFirmwareStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Bulb))
            return false;

        Bulb that = (Bulb) obj;

        return that.macAddress.equals(this.macAddress);
    }

    @Override
    public int hashCode() {
        return macAddress.hashCode();
    }

    private void connect() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket();
            socket.connect(address, BulbDiscoveryService.getGatewayDiscoveryPort());
            socket.setSoTimeout(0);
            socket.setReuseAddress(true);
            packetReader = new PacketReader(socket);
            packetReader.start();
            packetReader.waitForStartup();
        }
    }

    public void disconnect() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        packetReader.stopRunning();
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
        connect();
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
