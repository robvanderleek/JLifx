package jlifx.packet;

import static org.mockito.Mockito.mock;

import java.awt.Color;
import java.net.InetAddress;

import org.junit.Test;
import org.mockito.Mockito;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.GatewayBulb;

public class PacketServiceTest extends AbstractJLifxTestCase {

    @Test
    public void testSendPowerManagementPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        PacketWriter packetWriter = mock(PacketWriter.class);

        packetService.setPacketWriter(packetWriter);

        packetService.sendPowerManagementPacket(bulb, true);

        Mockito.verify(packetWriter).sendPacket(Mockito.isA(GatewayBulb.class),
            Mockito.isA(PowerManagementPacket.class));
    }

    @Test
    public void testSendColorManagementPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        PacketWriter packetWriter = mock(PacketWriter.class);
        packetService.setPacketWriter(packetWriter);

        packetService.sendColorManagementPacket(bulb, Color.BLUE, 3, 0.5F);

        Mockito.verify(packetWriter).sendPacket(Mockito.isA(GatewayBulb.class),
            Mockito.isA(ColorManagementPacket.class));
    }

    @Test
    public void testSendSetDimAbsolutePacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        PacketWriter packetWriter = mock(PacketWriter.class);
        packetService.setPacketWriter(packetWriter);

        packetService.sendSetDimAbsolutePacket(bulb, 0.5F);

        Mockito.verify(packetWriter).sendPacket(Mockito.isA(GatewayBulb.class),
            Mockito.isA(SetDimAbsolutePacket.class));
    }

    @Test
    public void testSendStatusRequestPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        PacketWriter packetWriter = mock(PacketWriter.class);
        packetService.setPacketWriter(packetWriter);

        packetService.sendStatusRequestPacket(bulb);

        Mockito.verify(packetWriter).sendPacketAndWaitForResponse(Mockito.isA(GatewayBulb.class),
            Mockito.isA(StatusRequestPacket.class));
    }

    @Test
    public void testSendWifiInfoRequestPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        PacketWriter packetWriter = mock(PacketWriter.class);
        packetService.setPacketWriter(packetWriter);

        packetService.sendWifiInfoRequestPacket(bulb);

        Mockito.verify(packetWriter).sendPacketAndWaitForResponse(Mockito.isA(GatewayBulb.class),
            Mockito.isA(WifiInfoRequestPacket.class));
    }

}