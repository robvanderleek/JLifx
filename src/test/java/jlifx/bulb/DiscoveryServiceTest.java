package jlifx.bulb;

import java.net.InetAddress;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

public class DiscoveryServiceTest extends TestCase {

    @Test
    public void testGetNetworkBroadcastAddresses() throws Exception {
        List<InetAddress> result = DiscoveryService.getNetworkBroadcastAddresses();

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

}
