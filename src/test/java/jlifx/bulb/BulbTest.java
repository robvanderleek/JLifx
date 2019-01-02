package jlifx.bulb;

import jlifx.packet.PacketService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BulbTest extends AbstractJLifxTestCase {

    @Test
    public void testBulbOperations() throws Exception {
        GatewayBulb gatewayBulb = getMockedGatewayBulb();
        Bulb bulb = new Bulb(TEST_MAC_ADDRESS_1, gatewayBulb);

        assertEquals(TEST_MAC_ADDRESS_1, bulb.getMacAddress());
        assertEquals(gatewayBulb, bulb.getGatewayBulb());
        assertEquals(bulb, bulb);
    }

    @Test
    public void testSwitchBulbOnOff() throws Exception {
        GatewayBulb gatewayBulb = getMockedGatewayBulb();
        PacketService packetService = getMockedPacketService();
        Bulb bulb = new Bulb(TEST_MAC_ADDRESS_1, gatewayBulb);
        when(gatewayBulb.getPacketService()).thenReturn(packetService);

        bulb.switchOn();
        bulb.switchOff();

        verify(packetService).sendPowerManagementPacket(bulb, true);
        verify(packetService).sendPowerManagementPacket(bulb, false);
    }

}