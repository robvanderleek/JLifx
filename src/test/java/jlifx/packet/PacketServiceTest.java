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

import jlifx.bulb.GatewayBulb;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Test;

public class PacketServiceTest extends EasyMockSupport {
    private static final byte[] TEST_MAC_ADDRESS_1 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
    private static final byte[] TEST_MAC_ADDRESS_2 = new byte[] {0x06, 0x05, 0x04, 0x03, 0x02, 0x01};

    private PacketWriter buildSimplePacketWriterMock(Class<? extends Packet> packetClass) throws UnknownHostException,
        IOException {
        PacketWriter result = createMock(PacketWriter.class);
        result.connect(InetAddress.getLocalHost());
        result.sendPacket(isA(GatewayBulb.class), isA(packetClass));
        expectLastCall().atLeastOnce();
        return result;
    }

    @Test
    public void testSendPowerManagementPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        packetService.setPacketWriter(buildSimplePacketWriterMock(PowerManagementPacket.class));
        replayAll();

        packetService.sendPowerManagementPacket(bulb, true);

        verifyAll();
    }

    @Test
    public void testSendColorManagementPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        packetService.setPacketWriter(buildSimplePacketWriterMock(ColorManagementPacket.class));
        replayAll();

        packetService.sendColorManagementPacket(bulb, Color.BLUE, 3, 0.5F);

        verifyAll();
    }

    @Test
    public void testSendSetDimAbsolutePacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        packetService.setPacketWriter(buildSimplePacketWriterMock(SetDimAbsolutePacket.class));
        replayAll();

        packetService.sendSetDimAbsolutePacket(bulb, 0.5F);

        verifyAll();
    }

    @Test
    public void testSendStatusRequestPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);
        PacketService packetService = new PacketService();
        PacketWriter packetWriter = createMock(PacketWriter.class);
        List<Packet> returnValue = Collections.singletonList(new StatusResponsePacket(new Packet()));
        EasyMock.expect(
            packetWriter.sendPacketAndWaitForResponse(isA(GatewayBulb.class), isA(StatusRequestPacket.class)))
            .andReturn(
            returnValue);
        packetService.setPacketWriter(packetWriter);
        replayAll();
        
        List<StatusResponsePacket> result = packetService.sendStatusRequestPacket(bulb);

        verifyAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}