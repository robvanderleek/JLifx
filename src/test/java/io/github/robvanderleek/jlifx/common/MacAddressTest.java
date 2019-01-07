package io.github.robvanderleek.jlifx.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MacAddressTest {

    @Test
    void testToString() {
        MacAddress macAddress = new MacAddress((byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
                (byte) 0x06);

        assertEquals("01:02:03:04:05:06", macAddress.toString());
    }
}