package jlifx.packet;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.GatewayBulb;

public class PacketServiceTest extends AbstractJLifxTestCase {

    private PacketWriter buildPacketWriterMock(Class<? extends Packet> packetClass) throws UnknownHostException,
        IOException {
        PacketWriter result = createMock(PacketWriter.class);
        result.connect(InetAddress.getLocalHost());
        result.sendPacket(isA(GatewayBulb.class), isA(packetClass));
        expectLastCall().atLeastOnce();
        return result;
    }

    private PacketWriter buildPacketWriterMockWithResponse(Class<? extends Packet> packetClass, Packet responsePacket)
        throws UnknownHostException, IOException {
        PacketWriter result = createMock(PacketWriter.class);
        List<Packet> returnValue = Collections.singletonList(responsePacket);
        EasyMock.expect(result.sendPacketAndWaitForResponse(isA(GatewayBulb.class), isA(packetClass)))
            .andReturn(returnValue);
        return result;
    }

    @Test
    public void testSendPowerManagementPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        packetService.setPacketWriter(buildPacketWriterMock(PowerManagementPacket.class));
        replayAll();

        packetService.sendPowerManagementPacket(bulb, true);

        verifyAll();
    }

    @Test
    public void testSendColorManagementPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        packetService.setPacketWriter(buildPacketWriterMock(ColorManagementPacket.class));
        replayAll();

        packetService.sendColorManagementPacket(bulb, Color.BLUE, 3, 0.5F);

        verifyAll();
    }

    @Test
    public void testSendSetDimAbsolutePacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        packetService.setPacketWriter(buildPacketWriterMock(SetDimAbsolutePacket.class));
        replayAll();

        packetService.sendSetDimAbsolutePacket(bulb, 0.5F);

        verifyAll();
    }

    @Test
    public void testSendStatusRequestPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        packetService.setPacketWriter(buildPacketWriterMockWithResponse(StatusRequestPacket.class,
            new StatusResponsePacket(new Packet())));
        replayAll();

        List<StatusResponsePacket> result = packetService.sendStatusRequestPacket(bulb);

        verifyAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testSendWifiInfoRequestPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        packetService.setPacketWriter(buildPacketWriterMockWithResponse(WifiInfoRequestPacket.class,
            new WifiInfoResponsePacket(new Packet())));
        replayAll();

        List<Packet> result = packetService.sendWifiInfoRequestPacket(bulb);

        verifyAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}