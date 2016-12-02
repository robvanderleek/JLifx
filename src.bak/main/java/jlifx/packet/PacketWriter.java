package jlifx.packet;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import jlifx.bulb.GatewayBulb;

public interface PacketWriter {

    void connect(InetAddress address) throws IOException;

    void sendPacket(GatewayBulb gatewayBulb, Packet packet) throws IOException;

    List<Packet> sendPacketAndWaitForResponse(GatewayBulb gatewayBulb, Packet packet) throws IOException;

    void close() throws IOException;

}