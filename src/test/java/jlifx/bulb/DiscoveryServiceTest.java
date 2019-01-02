package jlifx.bulb;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import jlifx.boblightd.NetworkUtils;
import jlifx.packet.StatusResponsePacket;
import jlifx.packet.StatusResponsePacketTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DiscoveryServiceTest extends AbstractJLifxTestCase {
    private int testDiscoveryPort;

    @Before
    public void setup() {
        testDiscoveryPort = getFreeLocalPort();
        DiscoveryService.setGatewayDiscoveryPort(testDiscoveryPort);
        DiscoveryService.setIgnoreGatewaysOnLocalhost(false);
    }

    @After
    public void tearDown() {
        DiscoveryService.setGatewayDiscoveryPort(56700);
        DiscoveryService.setIgnoreGatewaysOnLocalhost(true);
    }

    private static class TestGatewayBulbChannelHandlerAdapter extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
            DatagramSocket socket = new DatagramSocket();
            StatusResponsePacket packet = StatusResponsePacketTest.makeTestPacket();
            packet.setTargetMac(TEST_MAC_ADDRESS_1);
            socket.send(packet.toDatagramPacket(msg.sender().getAddress()));
            socket.close();
        }

    }

    @Test
    public void testDiscoverGatewayBulb() throws Exception {
        ChannelFuture channelFuture = NetworkUtils.startUdpServer(testDiscoveryPort,
                new TestGatewayBulbChannelHandlerAdapter());

        GatewayBulb result = DiscoveryService.discoverGatewayBulb();

        assertNotNull(result);
        assertEquals(TEST_MAC_ADDRESS_1, result.getMacAddress());

        channelFuture.channel().close().sync();
    }

    @Test
    public void testGetNetworkBroadcastAddresses() {
        List<InetAddress> result = DiscoveryService.getNetworkBroadcastAddresses();

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

}
