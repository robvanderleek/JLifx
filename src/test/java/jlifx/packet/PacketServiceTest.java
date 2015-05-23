package jlifx.packet;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;

import java.net.InetAddress;

import jlifx.bulb.GatewayBulb;

import org.easymock.EasyMockSupport;
import org.junit.Test;

public class PacketServiceTest extends EasyMockSupport {
    private static final byte[] TEST_MAC_ADDRESS_1 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
    private static final byte[] TEST_MAC_ADDRESS_2 = new byte[] {0x06, 0x05, 0x04, 0x03, 0x02, 0x01};

    @Test
    public void testSendPowerManagementPacket() throws Exception {
        GatewayBulb bulb = new GatewayBulb(InetAddress.getLocalHost(), TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2);

        PacketService packetService = new PacketService();
        PacketWriter packetWriter = createMock(PacketWriter.class);
        packetWriter.connect(InetAddress.getLocalHost());
        packetWriter.sendPacket(isA(GatewayBulb.class), isA(PowerManagementPacket.class));
        expectLastCall().atLeastOnce();
        packetService.setPacketWriter(packetWriter);
        replayAll();

        packetService.sendPowerManagementPacket(bulb, true);

        verifyAll();
    }
}