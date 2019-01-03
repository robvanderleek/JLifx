package io.github.robvanderleek.jlifx.packet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BitFieldTest {

    @Test
    public void concat() {
        BitField b1 = new BitField(3);
        BitField b2 = new BitField(4);
        b1.set(1, true);
        b2.set(2, true);

        BitField result = b1.concat(b2);

        assertEquals(7, result.getNumberOfBits());
        assertFalse(result.get(0));
        assertTrue(result.get(1));
        assertFalse(result.get(2));
        assertFalse(result.get(3));
        assertFalse(result.get(4));
        assertTrue(result.get(5));
        assertFalse(result.get(6));
    }

    @Test
    public void testToString() {
        BitField bitField = new BitField(3);
        bitField.set(0, true);
        bitField.set(2, true);

        assertEquals("101", bitField.toString());

        bitField = new BitField(3);
        bitField.set(0, true);

        assertEquals("100", bitField.toString());

        bitField = new BitField(3);
        bitField.set(2, true);

        assertEquals("001", bitField.toString());
    }

    @Test
    public void toByteArray() {
        BitField b1 = new BitField(3);
        b1.set(0, true);
        b1.set(2, true);

        byte[] result = b1.toByteArray();

        assertEquals(1, result.length);
        assertEquals((byte)0xA0, result[0]);
    }

}