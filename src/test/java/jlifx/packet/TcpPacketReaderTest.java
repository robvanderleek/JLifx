package jlifx.packet;

import jlifx.bulb.AbstractJLifxTestCase;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TcpPacketReaderTest extends AbstractJLifxTestCase {

    @Test
    public void testNoPacket() throws Exception {
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(new byte[]{}));

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

        assertEquals(TEST_MAC_ADDRESS_1, receivedPacket.getTargetMac());
        assertTrue(receivedPacket.on());
    }

    private final class InputStreamExtension extends InputStream {
        private int request = 0;

        @Override
        public int read() {
            return 0;
        }

        private int copy(byte[] src, byte[] dst) {
            System.arraycopy(src, 0, dst, 0, src.length);
            return src.length;
        }

        @Override
        public int read(byte[] b) {
            if (request == 0) {
                request++;
                PowerManagementPacket packet = new PowerManagementPacket(TEST_MAC_ADDRESS_1, true);
                byte[] bytes = packet.toByteArray();
                return copy(bytes, b);
            } else if (request == 1) {
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

        assertEquals(TEST_MAC_ADDRESS_1, receivedPacket.getTargetMac());
        assertTrue(receivedPacket.on());

        receivedPacket = new PowerManagementPacket(receivedPackets.get(1));

        assertEquals(TEST_MAC_ADDRESS_2, receivedPacket.getTargetMac());
        assertFalse(receivedPacket.on());
    }

}