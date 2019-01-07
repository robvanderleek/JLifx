package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.utils.GatewayBulbMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BulbDiscoveryServiceTest extends AbstractJLifxTestCase {
    private int testDiscoveryPort;

    @Before
    public void setup() {
        testDiscoveryPort = getFreeLocalPort();
        BulbDiscoveryService.setGatewayDiscoveryPort(testDiscoveryPort);
        BulbDiscoveryService.setIgnoreGatewaysOnLocalhost(false);
    }

    @After
    public void tearDown() {
        BulbDiscoveryService.setGatewayDiscoveryPort(56700);
        BulbDiscoveryService.setIgnoreGatewaysOnLocalhost(true);
    }

    @Test
    public void testDiscoverGatewayBulb() throws SocketException {
        GatewayBulbMock gatewayBulbMock = new GatewayBulbMock(testDiscoveryPort);
        gatewayBulbMock.run();

        GatewayBulb result = BulbDiscoveryService.discoverGatewayBulb();

        assertNotNull(result);
        assertEquals(TEST_MAC_ADDRESS_1, result.getMacAddress());

        gatewayBulbMock.stop();
    }

    @Test
    public void discoverBulbByName() throws SocketException {
        GatewayBulbMock gatewayBulbMock = new GatewayBulbMock(testDiscoveryPort);
        gatewayBulbMock.run();

        GatewayBulb gatewayBulb = BulbDiscoveryService.discoverGatewayBulb();
        Optional<Bulb> result = BulbDiscoveryService.discoverBulbByName(gatewayBulb, "hello");

        assertTrue(result.isPresent());
        assertEquals("hello", result.get().getName());
        assertEquals(TEST_MAC_ADDRESS_1, result.get().getMacAddress());

        gatewayBulbMock.stop();
    }

    @Test
    public void discoverBulbByNameIsCaseInsensitive() throws SocketException {
        GatewayBulbMock gatewayBulbMock = new GatewayBulbMock(testDiscoveryPort);
        gatewayBulbMock.run();

        GatewayBulb gatewayBulb = BulbDiscoveryService.discoverGatewayBulb();
        Optional<Bulb> result = BulbDiscoveryService.discoverBulbByName(gatewayBulb, "Hello");

        assertTrue(result.isPresent());
        assertEquals("hello", result.get().getName());
        assertEquals(TEST_MAC_ADDRESS_1, result.get().getMacAddress());

        gatewayBulbMock.stop();
    }

    @Test
    public void testGetNetworkBroadcastAddresses() {
        List<InetAddress> result = BulbDiscoveryService.getNetworkBroadcastAddresses();

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
}
