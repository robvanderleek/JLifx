package jlifx.packet;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jlifx.bulb.BulbMeshFirmwareStatus;
import jlifx.bulb.GatewayBulb;
import jlifx.bulb.IBulb;

public final class PacketService {
    private PacketWriter packetWriter = new UdpTcpPacketWriter();

    public void setPacketWriter(PacketWriter packetWriter) {
        this.packetWriter = packetWriter;
    }

    public void sendPowerManagementPacket(IBulb bulb, boolean on) throws IOException {
        Packet packet = new PowerManagementPacket(bulb.getMacAddress(), on);
        packetWriter.connect(bulb.getGatewayBulb().getInetAddress());
        packetWriter.sendPacket(bulb.getGatewayBulb(), packet);
    }

    public void sendColorManagementPacket(IBulb bulb, Color color, int fadetime, float brightness) throws IOException {
        Packet packet = new ColorManagementPacket(bulb.getMacAddress(), color, fadetime, brightness);
        packetWriter.connect(bulb.getGatewayBulb().getInetAddress());
        packetWriter.sendPacket(bulb.getGatewayBulb(), packet);
    }

    public void sendSetDimAbsolutePacket(IBulb bulb, float brightness) throws IOException {
        Packet packet = new SetDimAbsolutePacket(brightness);
        packetWriter.connect(bulb.getGatewayBulb().getInetAddress());
        packetWriter.sendPacket(bulb.getGatewayBulb(), packet);
    }

    public List<StatusResponsePacket> sendStatusRequestPacket(GatewayBulb bulb) throws IOException {
        Packet packet = new StatusRequestPacket();
        List<Packet> responsePackets = packetWriter.sendPacketAndWaitForResponse(bulb, packet);
        List<StatusResponsePacket> result = new ArrayList<StatusResponsePacket>();
        responsePackets.forEach(p -> result.add(new StatusResponsePacket(p)));
        return result;
    }

    public List<Packet> sendWifiInfoRequestPacket(GatewayBulb bulb) throws IOException {
        Packet packet = new WifiInfoRequestPacket();
        return packetWriter.sendPacketAndWaitForResponse(bulb, packet);
    }

    public BulbMeshFirmwareStatus getMeshFirmwareStatus(GatewayBulb bulb) throws IOException {
        Packet packet = new MeshFirmwareRequestPacket();
        List<Packet> responsePackets = packetWriter.sendPacketAndWaitForResponse(bulb, packet);
        if (!responsePackets.isEmpty()) {
            return BulbMeshFirmwareStatus.fromPacket(responsePackets.get(0));
        } else {
            return null;
        }
    }

    public void close() throws IOException {
        packetWriter.close();
    }

}