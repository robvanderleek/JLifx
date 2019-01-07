package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.utils.GatewayBulbMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BulbDiscoveryServiceTest extends AbstractJLifxTestCase {
    private int testDiscoveryPort;

    @BeforeEach
    public void setup() {
        testDiscoveryPort = getFreeLocalPort();
        BulbDiscoveryService.setGatewayDiscoveryPort(testDiscoveryPort);
        BulbDiscoveryService.setIgnoreGatewaysOnLocalhost(false);
    }

    @AfterEach
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
