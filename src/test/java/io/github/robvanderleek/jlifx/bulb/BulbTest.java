package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.packet.PacketService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class BulbTest extends AbstractJLifxTestCase {

    @Test
    void switchBulbOnOff() throws Exception {
        Bulb bulb = getNewTestBulb();
        PacketService packetService = getMockedPacketService();
        bulb.setPacketService(packetService);

        bulb.switchOn();
        bulb.switchOff();

        verify(packetService).sendPowerManagementPacket(bulb, true);
        verify(packetService).sendPowerManagementPacket(bulb, false);
    }

}