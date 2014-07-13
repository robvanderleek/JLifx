package jlifx.packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

class UdpPacketReader extends Thread implements Runnable, PacketReader {
    private DatagramSocket datagramSocket;
    private List<Packet> receivedPackets = new ArrayList<Packet>();
    private Object monitor = new Object();

    public UdpPacketReader(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public void sync() {
        try {
            synchronized (monitor) {
                monitor.wait();
            }
        } catch (InterruptedException e) {
            e = null; /* do nothing */
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[128];
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                receivedPackets.add(Packet.fromDatagramPacket(datagramPacket));
            }
        } catch (SocketTimeoutException e) {
            e = null; /* do nothing */
        } catch (IOException e) {
            e = null; /* do nothing */
        }
        synchronized (monitor) {
            monitor.notify();
        }
    }

    public List<Packet> getReceivedPackets() {
        return receivedPackets;
    }

}