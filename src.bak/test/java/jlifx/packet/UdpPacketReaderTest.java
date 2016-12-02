package jlifx.packet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import jlifx.bulb.AbstractJLifxTestCase;

public class UdpPacketReaderTest extends AbstractJLifxTestCase {

    private class DatagramSocketStub extends DatagramSocket {
        private final Iterator<Packet> packetIterator;

        public DatagramSocketStub(List<Packet> packets) throws SocketException {
            this.packetIterator = packets.iterator();
        }

        @Override
        public synchronized void receive(DatagramPacket p) throws IOException {
            if (packetIterator.hasNext()) {
                p.setData(packetIterator.next().toByteArray());
            } else {
                throw new SocketTimeoutException();
            }
        }

    }

    @Test
    public void testNoPacket() throws Exception {
        UdpPacketReader packetReader = new UdpPacketReader(new DatagramSocketStub(new ArrayList<Packet>()));
        packetReader.start();
        Thread.sleep(500);
        List<Packet> receivedPackets = packetReader.getReceivedPackets();
        packetReader.stopRunning();

        assertTrue(receivedPackets.isEmpty());
    }

    @Test
    public void testOnePacket() throws Exception {
        DatagramSocket datagramSocket = new DatagramSocketStub(Collections.singletonList(new StatusRequestPacket()));
        UdpPacketReader packetReader = new UdpPacketReader(datagramSocket);
        packetReader.start();
        Thread.sleep(500);
        List<Packet> receivedPackets = packetReader.getReceivedPackets();
        packetReader.stopRunning();

        assertFalse(receivedPackets.isEmpty());
        assertEquals(1, receivedPackets.size());

        Packet receivedPacket = receivedPackets.get(0);

        assertEquals(StatusRequestPacket.TYPE, receivedPacket.getType());
    }

    @Test
    public void testTwoPackets() throws Exception {
        List<Packet> packets = new ArrayList<Packet>();
        packets.add(new StatusRequestPacket());
        packets.add(new PowerManagementPacket(TEST_MAC_ADDRESS_1, true));
        UdpPacketReader packetReader = new UdpPacketReader(new DatagramSocketStub(packets));
        packetReader.start();
        Thread.sleep(500);
        List<Packet> receivedPackets = packetReader.getReceivedPackets();
        packetReader.stopRunning();

        assertFalse(receivedPackets.isEmpty());
        assertEquals(2, receivedPackets.size());

        Packet receivedPacket = receivedPackets.get(0);

        assertEquals(StatusRequestPacket.TYPE, receivedPacket.getType());

        receivedPacket = receivedPackets.get(1);

        assertEquals(PowerManagementPacket.TYPE, receivedPacket.getType());
    }

}
