package jlifx.packet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PacketReader extends Thread implements Runnable {
    private static final Log LOG = LogFactory.getLog(PacketReader.class);
    private DatagramSocket datagramSocket;
    private List<Packet> receivedPackets = new LinkedList<>();
    private final Object lock = new Object();
    private boolean keepRunning = true;

    public PacketReader(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public synchronized void waitForStartup() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[128];
            while (keepRunning) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                synchronized (lock) {
                    lock.notify();
                }
                try {
                    datagramSocket.receive(datagramPacket);
                } catch (SocketException e) {
                    continue;
                }
                if (receivedPackets.size() >= 10) {
                    receivedPackets.remove(0);
                }
                receivedPackets.add(Packet.fromDatagramPacket(datagramPacket));
            }
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void stopRunning() {
        keepRunning = false;
    }

    List<Packet> getReceivedPackets() {
        return receivedPackets;
    }

    public boolean hasAcknowledgement(byte sequenceNumber) {
        return receivedPackets.stream().anyMatch(p -> p.getType() == 0x2D && p.getSequenceNumber() == sequenceNumber);
    }

    public Optional<Packet> getResponsePacket(byte sequenceNumber, byte responseType) {
        return receivedPackets.stream()
                              .filter(p -> p.getType() == responseType && p.getSequenceNumber() == sequenceNumber)
                              .findAny();
    }

    public List<Packet> getResponsePackets(byte responseType) {
        return receivedPackets.stream().filter(p -> p.getType() == responseType).collect(Collectors.toList());
    }

}