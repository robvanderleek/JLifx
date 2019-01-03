package io.github.robvanderleek.jlifx.packet;

import org.junit.Test;

import java.awt.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class PacketTest {
    private static final MacAddress TEST_MAC_ADDRESS = new MacAddress(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06});
    private static final byte TEST_TYPE = 0x12;

    @Test
    public void testCreateEmptyPacket() {
        Packet packet = new Packet();

        assertEquals(MacAddress.ALL_BULBS, packet.getTargetMac());
    }

    @Test
    public void testToAndFromByteArray() {
        Packet packet = new Packet(TEST_MAC_ADDRESS, TEST_TYPE);

        byte[] byteArray = packet.toByteArray();

        assertNotNull(byteArray);

        Packet fromByteArray = Packet.fromByteArray(byteArray);

        assertEquals(TEST_MAC_ADDRESS, fromByteArray.getTargetMac());
        assertEquals(TEST_TYPE, fromByteArray.getType());
    }

    @Test
    public void testToAndFromDatagram() {
        Packet packet = new Packet(TEST_MAC_ADDRESS, TEST_TYPE);
        InetAddress address = InetAddress.getLoopbackAddress();

        DatagramPacket datagramPacket = packet.toDatagramPacket(address);

        assertNotNull(datagramPacket);
        assertEquals(address, datagramPacket.getAddress());

        Packet result = Packet.fromDatagramPacket(datagramPacket);

        assertEquals(packet.getTargetMac(), result.getTargetMac());
        assertEquals(packet.getType(), result.getType());
    }

    @Test
    public void testBuildColorManagementPacket() {
        Packet packet = new ColorManagementPacket(TEST_MAC_ADDRESS, Color.BLUE, 10, 0.5F);

        assertNotNull(packet);
    }

    @Test
    public void testBuildMeshFirmwareRequestPacket() {
        Packet packet = new MeshFirmwareRequestPacket();

        assertNotNull(packet);
    }

    @Test
    public void testBuildPowerManagementPacket() {
        Packet packet = new PowerManagementPacket(TEST_MAC_ADDRESS, false);

        assertNotNull(packet);
    }

    @Test
    public void testBuildSetDimAbsolutePacket() {
        Packet packet = new SetDimAbsolutePacket(0.5F);

        assertNotNull(packet);
    }

    @Test
    public void testBuildStatusRequestPacket() {
        Packet packet = new StatusRequestPacket();

        assertNotNull(packet);
    }
}