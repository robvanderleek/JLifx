package jlifx.packet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.InetAddress;

import org.junit.Test;


public class PacketTest {
    private static final byte[] EMPTY_MAC_ADDRESS = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static final byte[] TEST_MAC_ADDRESS_1 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
    private static final byte[] TEST_MAC_ADDRESS_2 = new byte[] {0x06, 0x05, 0x04, 0x03, 0x02, 0x01};
    private static final byte[] TEST_TIMESTAMP = new byte[] {0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, (byte)0x80};
    private static final byte TEST_TYPE = 0x12;

    @Test
    public void testCreateEmptyPacket() throws Exception {
        Packet packet = new Packet();

        assertArrayEquals(EMPTY_MAC_ADDRESS, packet.getTargetMac());
        assertArrayEquals(EMPTY_MAC_ADDRESS, packet.getGatewayMac());
    }

    @Test
    public void testToAndFromByteArray() throws Exception {
        Packet packet = new Packet(TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2, TEST_TIMESTAMP, TEST_TYPE);

        byte[] byteArray = packet.toByteArray();

        assertNotNull(byteArray);

        Packet fromByteArray = Packet.fromByteArray(byteArray);

        assertArrayEquals(TEST_MAC_ADDRESS_1, fromByteArray.getTargetMac());
        assertArrayEquals(TEST_MAC_ADDRESS_2, fromByteArray.getGatewayMac());
        assertArrayEquals(TEST_TIMESTAMP, fromByteArray.getTimestamp());
        assertEquals(TEST_TYPE, fromByteArray.getType());
    }

    @Test
    public void testToAndFromDatagram() throws Exception {
        Packet packet = new Packet(TEST_MAC_ADDRESS_1, TEST_MAC_ADDRESS_2, TEST_TIMESTAMP, TEST_TYPE);
        InetAddress address = InetAddress.getLoopbackAddress();

        DatagramPacket datagramPacket = packet.toDatagramPacket(address);

        assertNotNull(datagramPacket);
        assertEquals(address, datagramPacket.getAddress());

        Packet result = Packet.fromDatagramPacket(datagramPacket);

        assertArrayEquals(packet.getTargetMac(), result.getTargetMac());
        assertArrayEquals(packet.getGatewayMac(), result.getGatewayMac());
        assertArrayEquals(packet.getTimestamp(), result.getTimestamp());
        assertEquals(packet.getType(), result.getType());
    }

    @Test
    public void testBuildColorManagementPacket() throws Exception {
        Packet packet = new ColorManagementPacket(TEST_MAC_ADDRESS_1, Color.BLUE, 10, 0.5F);

        assertNotNull(packet);
    }

    @Test
    public void testBuildMeshFirmwareRequestPacket() throws Exception {
        Packet packet = new MeshFirmwareRequestPacket();

        assertNotNull(packet);
    }

    @Test
    public void testBuildPowerManagementPacket() throws Exception {
        Packet packet = new PowerManagementPacket(TEST_MAC_ADDRESS_1, false);

        assertNotNull(packet);
    }

    @Test
    public void testBuildSetDimAbsolutePacket() throws Exception {
        Packet packet = new SetDimAbsolutePacket(0.5F);

        assertNotNull(packet);
    }

    @Test
    public void testBuildStatusRequestPacket() throws Exception {
        Packet packet = new StatusRequestPacket();

        assertNotNull(packet);
    }

}