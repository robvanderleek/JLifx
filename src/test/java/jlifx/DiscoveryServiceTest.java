package jlifx;

import java.net.InetAddress;
import java.util.List;

import junit.framework.TestCase;

public class DiscoveryServiceTest extends TestCase {

	public void testGetNetworkBroadcastAddresses() throws Exception {
		List<InetAddress> result = DiscoveryService
				.getNetworkBroadcastAddresses();

		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

}
