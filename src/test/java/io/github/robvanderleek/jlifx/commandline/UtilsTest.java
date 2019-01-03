package io.github.robvanderleek.jlifx.commandline;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.packet.MacAddress;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void testParseMacAddress() {
        MacAddress macAddress = Utils.parseMacAddress("AA:BB:CC:DD:EE:FF");

        assertEquals((byte) 0xAA, macAddress.toByteArray()[0]);
        assertEquals((byte) 0xDD, macAddress.toByteArray()[3]);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidMacAddress() {
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
    public void testParseIpv4Address() {
        assertEquals(127, Utils.parseIpv4Address("127.0.0.196")[0] & 0xFF);
        assertEquals(0, Utils.parseIpv4Address("127.0.0.196")[1] & 0xFF);
        assertEquals(0, Utils.parseIpv4Address("127.0.0.196")[2] & 0xFF);
        assertEquals(196, Utils.parseIpv4Address("127.0.0.196")[3] & 0xFF);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidIpv4Address() {
        Utils.parseIpv4Address("127.0.cheese.cake");
    }

    @Test
    public void testStringToColorInvalidColorName() {
        assertNull(Utils.stringToColor("CheeseCake"));
    }

    @Test
    public void testWordToHexString() {
        assertEquals("$FFFF", Utils.wordToHexString(65535));
        assertEquals("$FF", Utils.wordToHexString(255));
        assertEquals("$0", Utils.wordToHexString(0));
    }

    @Test
    public void testGetMacAddressAsString() {
        Assert.assertEquals("01:02:03:04:05:06", AbstractJLifxTestCase.TEST_MAC_ADDRESS_1.toString());
    }

    @Test
    public void testIsFloatValue() {
        assertTrue(Utils.isFloatValue("0.123"));
        assertTrue(Utils.isFloatValue("1"));
        assertFalse(Utils.isFloatValue("Cheese Cake"));
    }

    @Test
    public void testIsIntegerValue() {
        assertTrue(Utils.isIntegerValue("123"));
        assertFalse(Utils.isIntegerValue("0.123"));
        assertFalse(Utils.isIntegerValue("Cheese Cake"));
    }

    @Test
    public void testFrom32bitsLittleEndian() {
        assertEquals(0x01020304, Utils.from32bitsLittleEndian((byte) 0x4, (byte) 0x3, (byte) 0x2, (byte) 0x1));
    }

}