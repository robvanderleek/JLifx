package io.github.robvanderleek.jlifx.packet;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.GatewayBulb;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;

public class PacketServiceTest extends AbstractJLifxTestCase {

    @Test
    public void testSendPowerManagementPacket() throws Exception {
        GatewayBulb bulb = getMockedGatewayBulb();

        new PacketService().sendPowerManagementPacket(bulb, true);

        Mockito.verify(bulb).sendPacket(Mockito.isA(PowerManagementPacket.class));
    }

    @Test
    public void testSendColorManagementPacket() throws Exception {
        GatewayBulb bulb = getMockedGatewayBulb();

        new PacketService().sendColorManagementPacket(bulb, Color.BLUE, 3, 0.5F);

        Mockito.verify(bulb).sendPacket(Mockito.isA(ColorManagementPacket.class));
    }

    @Test
    public void testSendSetDimAbsolutePacket() throws Exception {
        GatewayBulb bulb = getMockedGatewayBulb();

        new PacketService().sendSetDimAbsolutePacket(bulb, 0.5F);

        Mockito.verify(bulb).sendPacket(Mockito.isA(SetDimAbsolutePacket.class));
    }

    @Test
    public void testSendStatusRequestPacket() throws Exception {
        GatewayBulb bulb = getMockedGatewayBulb();

        new PacketService().sendStatusRequestPacket(bulb);

        Mockito.verify(bulb).sendPacketAndGetResponses(Mockito.isA(StatusRequestPacket.class));
    }

    @Test
    public void testSendWifiInfoRequestPacket() throws Exception {
        GatewayBulb bulb = getMockedGatewayBulb();

        new PacketService().sendWifiInfoRequestPacket(bulb);

        Mockito.verify(bulb).sendPacketAndGetResponse(Mockito.isA(WifiInfoRequestPacket.class));
    }

}