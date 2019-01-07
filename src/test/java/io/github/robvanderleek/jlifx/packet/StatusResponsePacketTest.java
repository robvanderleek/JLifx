package io.github.robvanderleek.jlifx.packet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusResponsePacketTest {

    public static StatusResponsePacket makeTestPacket() {
        Packet packet = new Packet();
        byte[] payload = new byte[]{0x12, 0x34, // hue (LE)
                0x43, 0x21, // saturation (LE)
                0x12, 0x34, // brightness (LE)
                0x12, 0x34, // kelvin (LE)
                0x02, 0x01, // dim (LE)
                (byte) 0xFF, (byte) 0xFF, // power
                'h', 'e', 'l', 'l', 'o', 0x00 // label
        };
        packet.setPayload(payload);
        packet.setType((byte) 0x6B);
        return new StatusResponsePacket(packet);
    }

    @Test
    public void testGetBulbName() {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals("hello", packet.getBulbName());
    }

    @Test
    public void testGetHue() {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x3412, packet.getHue());
    }

    @Test
    public void testGetSaturation() {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x2143, packet.getSaturation());
    }

    @Test
    public void testGetBrightness() {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x3412, packet.getBrightness());
    }

    @Test
    public void testGetKelvin() {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x3412, packet.getKelvin());
    }

    @Test
    public void testGetDim() {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x0102, packet.getDim());
    }

    @Test
    public void testGetPower() {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0xFFFF, packet.getPower());
    }

}