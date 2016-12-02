package jlifx.packet;

public class MeshFirmwareRequestPacket extends Packet {

    public MeshFirmwareRequestPacket() {
        setType((byte)0x0e);
    }

}