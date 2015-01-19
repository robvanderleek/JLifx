package jlifx.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void testIsValidIpv4Address() {
        assertTrue(Utils.isValidIpv4Address("127.0.0.1"));
        assertFalse(Utils.isValidIpv4Address("127.0.0"));
        assertFalse(Utils.isValidIpv4Address("cheese-cake"));
    }

    @Test
    public void testIsValidMacAddress() {
        assertTrue(Utils.isValidMacAddress("AA:BB:CC:DD:EE:FF"));
        assertTrue(Utils.isValidMacAddress("aa:bb:cc:dd:ee:ff"));
        assertTrue(Utils.isValidMacAddress("01:23:45:67:89:ff"));
        assertFalse(Utils.isValidMacAddress("cheese-cake"));
    }

    @Test
    public void testParseIpv4Address() throws Exception {
        assertEquals(127, Utils.parseIpv4Address("127.0.0.196")[0] & 0xFF);
        assertEquals(0, Utils.parseIpv4Address("127.0.0.196")[1] & 0xFF);
        assertEquals(0, Utils.parseIpv4Address("127.0.0.196")[2] & 0xFF);
        assertEquals(196, Utils.parseIpv4Address("127.0.0.196")[3] & 0xFF);
    }

}
