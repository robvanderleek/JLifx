package jlifx.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jlifx.bulb.AbstractJLifxTestCase;

public class UtilsTest {

    @Test
    public void testParseMacAddress() throws Exception {
        byte[] macAddress = Utils.parseMacAddress("AA:BB:CC:DD:EE:FF");

        assertEquals((byte)0xAA, macAddress[0]);
        assertEquals((byte)0xDD, macAddress[3]);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidMacAddress() throws Exception {
        Utils.parseMacAddress("GG:HH:CC:DD:EE:FF");
    }

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

    @Test
    public void testParseInvalidIpv4Address() throws Exception {
        assertNull(Utils.parseIpv4Address("127.0.cheese.cake"));
    }

    @Test
    public void testStringToColorInvalidColorName() throws Exception {
        assertNull(Utils.stringToColor("CheeseCake"));
    }

    @Test
    public void testWordToHexString() throws Exception {
        assertEquals("$FFFF", Utils.wordToHexString(65535));
        assertEquals("$FF", Utils.wordToHexString(255));
        assertEquals("$0", Utils.wordToHexString(0));
    }

    @Test
    public void testGetMacAddressAsString() throws Exception {
        assertEquals("01:02:03:04:05:06", Utils.getMacAddressAsString(AbstractJLifxTestCase.TEST_MAC_ADDRESS_1));
    }

    @Test
    public void testIsFloatValue() throws Exception {
        assertTrue(Utils.isFloatValue("0.123"));
        assertTrue(Utils.isFloatValue("1"));
        assertFalse(Utils.isFloatValue("Cheese Cake"));
    }

    @Test
    public void testIsIntegerValue() throws Exception {
        assertTrue(Utils.isIntegerValue("123"));
        assertFalse(Utils.isIntegerValue("0.123"));
        assertFalse(Utils.isIntegerValue("Cheese Cake"));
    }

    @Test
    public void testFrom32bitsLittleEndian() throws Exception {
        assertEquals(0x01020304, Utils.from32bitsLittleEndian((byte)0x4, (byte)0x3, (byte)0x2, (byte)0x1));
    }
}