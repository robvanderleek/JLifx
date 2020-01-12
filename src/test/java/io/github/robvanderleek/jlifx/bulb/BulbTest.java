package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.packet.PacketService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class BulbTest extends AbstractJLifxTestCase {

    @Test
    void testSwitchBulbOnOff() throws Exception {
        Bulb bulb = new Bulb(TEST_INET_ADDRESS, TEST_MAC_ADDRESS);
        PacketService packetService = getMockedPacketService();
        bulb.setPacketService(packetService);

        bulb.switchOn();
        bulb.switchOff();

        verify(packetService).sendPowerManagementPacket(bulb, true);
        verify(packetService).sendPowerManagementPacket(bulb, false);
    }

}