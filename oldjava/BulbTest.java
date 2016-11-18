package jlifx.bulb;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jlifx.packet.PacketService;

public class BulbTest extends AbstractJLifxTestCase {

    @Test
    public void testBulbOperations() throws Exception {
        GatewayBulb gatewayBulb = getMockedGatewayBulb();
        Bulb bulb = new Bulb(TEST_MAC_ADDRESS_1, gatewayBulb);

        assertArrayEquals(TEST_MAC_ADDRESS_1, bulb.getMacAddress());
        assertEquals(gatewayBulb, bulb.getGatewayBulb());
        assertTrue(bulb.equals(bulb));
    }

    @Test
    public void testSwitchBulbOnOff() throws Exception {
        GatewayBulb gatewayBulb = getMockedGatewayBulb();
        PacketService packetService = getMockedPacketService();
        expect(gatewayBulb.getPacketService()).andReturn(packetService);
        expectLastCall().times(2);
        Bulb bulb = new Bulb(TEST_MAC_ADDRESS_1, gatewayBulb);
        packetService.sendPowerManagementPacket(bulb, true);
        packetService.sendPowerManagementPacket(bulb, false);
        replayAll();

        bulb.switchOn();
        bulb.switchOff();

        verifyAll();
    }

}