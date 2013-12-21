package lifx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Packet {
    private static final Log LOG = LogFactory.getLog(Packet.class);
    private byte[] size = new byte[] {0x24, 0x00};
    private byte[] protocol = new byte[] {0x00, 0x34};
    private byte[] reserved = new byte[] {0x00, 0x00, 0x00, 0x00};
    private byte[] targetMac = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private byte[] reserved2 = new byte[] {0x00, 0x00};
    private byte[] gatewayMac = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private byte[] reserved3 = new byte[] {0x00, 0x00};
    private byte[] timestamp = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private byte[] type = new byte[] {0x02, 0x00};
    private byte[] reserved4 = new byte[] {0x00, 0x00};

    public Packet() {}

    public Packet(byte[] targetMac, byte[] type) {
        this.targetMac = targetMac;
        this.gatewayMac = targetMac;
        this.type = type;
    }

    public Packet(byte[] targetMac, byte[] gatewayMac, byte[] timestamp, byte[] type) {
        this.targetMac = targetMac;
        this.gatewayMac = gatewayMac;
        this.timestamp = timestamp;
        this.type = type;
    }

    public byte[] getTargetMac() {
        return targetMac;
    }

    public byte[] getGatewayMac() {
        return gatewayMac;
    }

    public byte[] getTimestamp() {
        return timestamp;
    }

    public byte[] getType() {
        return type;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(size);
            outputStream.write(protocol);
            outputStream.write(reserved);
            outputStream.write(targetMac);
            outputStream.write(reserved2);
            outputStream.write(gatewayMac);
            outputStream.write(reserved3);
            outputStream.write(timestamp);
            outputStream.write(type);
            outputStream.write(reserved4);
        } catch (IOException e) {
            LOG.error("Could not build Lifx packet");
        }
        return outputStream.toByteArray();
    }

    public static Packet fromDatagramPacket(DatagramPacket datagramPacket) {
        byte[] data = datagramPacket.getData();
        return new Packet(ArrayUtils.subarray(data, 8, 13), ArrayUtils.subarray(data, 16, 22), ArrayUtils.subarray(
            data, 23, 31), ArrayUtils.subarray(data, 31, 33));
    }

}
