package jlifx.packet;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

class TcpPacketReader extends Thread implements Runnable, PacketReader {
    private InputStream inputStream;
    private List<Packet> receivedPackets = new ArrayList<Packet>();
    private Object monitor = new Object();

    public TcpPacketReader(InputStream inputStream) {
        this.inputStream = inputStream;
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
                int length = inputStream.read(buffer);
                if (length > 0) {
                    receivedPackets.add(Packet.fromByteArray(buffer));
                }
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