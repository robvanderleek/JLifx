package jlifx.packet;

import java.util.List;

public interface PacketReader {

    void start();

    void sync();

    List<Packet> getReceivedPackets();

}