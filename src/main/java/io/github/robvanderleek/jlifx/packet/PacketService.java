package io.github.robvanderleek.jlifx.packet;

import io.github.robvanderleek.jlifx.bulb.BulbMeshFirmwareStatus;
import io.github.robvanderleek.jlifx.bulb.GatewayBulb;
import io.github.robvanderleek.jlifx.bulb.IBulb;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PacketService {

    public void sendPowerManagementPacket(IBulb bulb, boolean on) throws IOException {
        Packet packet = new PowerManagementPacket(bulb.getMacAddress(), on);
        bulb.getGatewayBulb().sendPacket(packet);
    }

    public void sendColorManagementPacket(IBulb bulb, Color color, int fadetime, float brightness) throws IOException {
        Packet packet = new ColorManagementPacket(bulb.getMacAddress(), color, fadetime, brightness);
        bulb.getGatewayBulb().sendPacket(packet);
    }

    public void sendSetDimAbsolutePacket(IBulb bulb, float brightness) throws IOException {
        Packet packet = new SetDimAbsolutePacket(brightness);
        bulb.getGatewayBulb().sendPacket(packet);
    }

    public List<StatusResponsePacket> sendStatusRequestPacket(GatewayBulb bulb) throws IOException {
        Packet packet = new StatusRequestPacket();
        List<Packet> responsePackets = bulb.sendPacketAndGetResponses(packet);
        return responsePackets.stream().map(StatusResponsePacket::new).collect(Collectors.toList());
    }

    Packet sendWifiInfoRequestPacket(GatewayBulb bulb) throws IOException {
        Packet packet = new WifiInfoRequestPacket();
        return bulb.sendPacketAndGetResponse(packet);
    }

    public BulbMeshFirmwareStatus getMeshFirmwareStatus(GatewayBulb bulb) throws IOException {
        Packet packet = new MeshFirmwareRequestPacket();
        Packet responsePacket = bulb.sendPacketAndGetResponse(packet);
        return BulbMeshFirmwareStatus.fromPacket(responsePacket);
    }

}