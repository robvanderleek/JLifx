package jlifx.packet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import jlifx.bulb.AbstractJLifxTestCase;

public class TcpPacketReaderTest extends AbstractJLifxTestCase {

    @Test
    public void testNoPacket() throws Exception {
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(new byte[] {}));

        TcpPacketReader packetReader = new TcpPacketReader(inputStream);
        packetReader.start();
        Thread.sleep(500);
        List<Packet> receivedPackets = packetReader.getReceivedPackets();
        packetReader.stopRunning();

        assertTrue(receivedPackets.isEmpty());
    }

    @Test
    public void testOnePacket() throws Exception {
        PowerManagementPacket packet = new PowerManagementPacket(TEST_MAC_ADDRESS_1, true);
        byte[] bytes = packet.toByteArray();
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(bytes));

        TcpPacketReader packetReader = new TcpPacketReader(inputStream);
        packetReader.start();
        Thread.sleep(500);
        List<Packet> receivedPackets = packetReader.getReceivedPackets();
        packetReader.stopRunning();

        assertFalse(receivedPackets.isEmpty());
        assertEquals(1, receivedPackets.size());

        PowerManagementPacket receivedPacket = new PowerManagementPacket(receivedPackets.get(0));

        assertArrayEquals(TEST_MAC_ADDRESS_1, receivedPacket.getTargetMac());
        assertTrue(receivedPacket.on());
    }

    private final class InputStreamExtension extends InputStream {
        private int request = 0;

        @Override
        public int read() throws IOException {
            return 0;
        }

        private int copy(byte[] src, byte[] dst) {
            for (int i = 0; i < src.length; i++) {
                dst[i] = src[i];
            }
            return src.length;
        }

        @Override
        public int read(byte[] b) throws IOException {
            if (request == 0) {
                request++;
                PowerManagementPacket packet = new PowerManagementPacket(TEST_MAC_ADDRESS_1, true);
                byte[] bytes = packet.toByteArray();
                return copy(bytes, b);
            } else
                if (request == 1) {
                request++;
                PowerManagementPacket packet = new PowerManagementPacket(TEST_MAC_ADDRESS_2, false);
                byte[] bytes = packet.toByteArray();
                return copy(bytes, b);
            } else {
                return -1;
            }
        }
    }

    @Test
    public void testTwoPackets() throws Exception {
        InputStream inputStream = new InputStreamExtension();

        TcpPacketReader packetReader = new TcpPacketReader(inputStream);
        packetReader.start();
        Thread.sleep(500);
        List<Packet> receivedPackets = packetReader.getReceivedPackets();
        packetReader.stopRunning();

        assertFalse(receivedPackets.isEmpty());
        assertEquals(2, receivedPackets.size());

        PowerManagementPacket receivedPacket = new PowerManagementPacket(receivedPackets.get(0));

        assertArrayEquals(TEST_MAC_ADDRESS_1, receivedPacket.getTargetMac());
        assertTrue(receivedPacket.on());

        receivedPacket = new PowerManagementPacket(receivedPackets.get(1));

        assertArrayEquals(TEST_MAC_ADDRESS_2, receivedPacket.getTargetMac());
        assertFalse(receivedPacket.on());
    }

}