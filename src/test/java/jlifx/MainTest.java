package jlifx;

import java.net.InetAddress;
import java.util.List;

import jlifx.Main;
import junit.framework.TestCase;

public class MainTest extends TestCase {

    public void testGetNetworkBroadcastAddresses() throws Exception {
        List<InetAddress> result = new Main().getNetworkBroadcastAddresses();

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

}
