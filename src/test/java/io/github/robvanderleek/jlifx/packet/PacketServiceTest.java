package io.github.robvanderleek.jlifx.packet;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.common.Color;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PacketServiceTest extends AbstractJLifxTestCase {

    @Test
    public void testSendPowerManagementPacket() throws Exception {
        Bulb bulb = getMockedBulb();

        new PacketService().sendPowerManagementPacket(bulb, true);

        Mockito.verify(bulb).sendPacket(Mockito.isA(PowerManagementPacket.class));
    }

    @Test
    public void testSendColorManagementPacket() throws Exception {
        Bulb bulb = getMockedBulb();

        new PacketService().sendColorManagementPacket(bulb, Color.BLUE, 3, 0.5F);

        Mockito.verify(bulb).sendPacket(Mockito.isA(ColorManagementPacket.class));
    }

    @Test
    public void testSendSetDimAbsolutePacket() throws Exception {
        Bulb bulb = getMockedBulb();

        new PacketService().sendSetDimAbsolutePacket(bulb, 0.5F);

        Mockito.verify(bulb).sendPacket(Mockito.isA(SetDimAbsolutePacket.class));
    }

    @Test
    public void testSendStatusRequestPacket() throws Exception {
        Bulb bulb = getMockedBulb();

        new PacketService().sendStatusRequestPacket(bulb);

        Mockito.verify(bulb).sendPacketAndGetResponses(Mockito.isA(StatusRequestPacket.class));
    }

    @Test
    public void testSendWifiInfoRequestPacket() throws Exception {
        Bulb bulb = getMockedBulb();

        new PacketService().sendWifiInfoRequestPacket(bulb);

        Mockito.verify(bulb).sendPacketAndGetResponse(Mockito.isA(WifiInfoRequestPacket.class));
    }

}