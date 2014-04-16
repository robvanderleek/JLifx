package jlifx.packet;

import junit.framework.TestCase;

import org.junit.Test;

public class StatusResponsePacketTest extends TestCase {

    private StatusResponsePacket makeTestPacket() {
        Packet packet = new Packet();
        byte[] payload = new byte[] {0x12, 0x34, // hue (LE)
            0x43, 0x21, // saturation (LE)
            0x12, 0x34, // brightness (LE)
            0x12, 0x34, // kelvin (LE)
            0x02, 0x01, // dim (LE)
            (byte)0xFF, (byte)0xFF, // power
            'h', 'e', 'l', 'l', 'o', 0x00 // label
        };
        packet.setPayload(payload);
        return new StatusResponsePacket(packet);
    }

    @Test
    public void testGetBulbName() throws Exception {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals("hello", packet.getBulbName());
    }

    @Test
    public void testGetHue() throws Exception {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x3412, packet.getHue());
    }

    @Test
    public void testGetSaturation() throws Exception {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x2143, packet.getSaturation());
    }

    @Test
    public void testGetBrightness() throws Exception {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x3412, packet.getBrightness());
    }

    @Test
    public void testGetKelvin() throws Exception {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x3412, packet.getKelvin());
    }

    @Test
    public void testGetDim() throws Exception {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0x0102, packet.getDim());
    }

    @Test
    public void testGetPower() throws Exception {
        StatusResponsePacket packet = makeTestPacket();

        assertEquals(0xFFFF, packet.getPower());
    }
}