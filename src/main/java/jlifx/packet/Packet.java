package jlifx.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import jlifx.bulb.DiscoveryService;
import jlifx.commandline.Utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Packet {
    private static final Log LOG = LogFactory.getLog(Packet.class);
    private byte[] protocol = new byte[] {0x00, 0x34};
    private byte[] reserved = new byte[] {0x00, 0x00, 0x00, 0x00};
    private byte[] targetMac = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private byte[] reserved2 = new byte[] {0x00, 0x00};
    private byte[] gatewayMac = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private byte[] reserved3 = new byte[] {0x00, 0x00};
    private byte[] timestamp = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private byte[] type = new byte[] {0x02, 0x00};
    private byte[] reserved4 = new byte[] {0x00, 0x00};
    private byte[] payload = new byte[] {};

    public Packet() {}

    public Packet(byte[] targetMac, byte[] gatewayMac, byte[] timestamp, byte type) {
        setTargetMac(targetMac);
        setGatewayMac(gatewayMac);
        setTimestamp(timestamp);
        setType(type);
    }

    public Packet(Packet packet) {
        setTargetMac(packet.getTargetMac());
        setGatewayMac(packet.getGatewayMac());
        setTimestamp(packet.getTimestamp());
        setType(packet.getType());
        setPayload(packet.getPayload());
    }

    public byte[] getTargetMac() {
        return targetMac;
    }

    public String getTargetMacAsString() {
        return Utils.getMacAddressAsString(targetMac);
    }

    public void setTargetMac(byte[] targetMac) {
        this.targetMac = targetMac;
    }

    public byte[] getGatewayMac() {
        return gatewayMac;
    }

    public void setGatewayMac(byte[] gatewayMac) {
        this.gatewayMac = gatewayMac;
    }

    public byte[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp) {
        this.timestamp = timestamp;
    }

    public byte getType() {
        return type[0];
    }

    public void setType(byte type) {
        this.type = new byte[] {type, 0x00};
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(new byte[] {(byte)(0x24 + payload.length), 0x00});
            outputStream.write(protocol);
            outputStream.write(reserved);
            outputStream.write(targetMac);
            outputStream.write(reserved2);
            outputStream.write(gatewayMac);
            outputStream.write(reserved3);
            outputStream.write(timestamp);
            outputStream.write(type);
            outputStream.write(reserved4);
            outputStream.write(payload);
        } catch (IOException e) {
            LOG.error("Could not build LIFX packet");
        }
        return outputStream.toByteArray();
    }

    public static Packet fromDatagramPacket(DatagramPacket datagramPacket) {
        return fromByteArray(datagramPacket.getData());
    }

    public DatagramPacket toDatagramPacket(InetAddress address) {
        byte[] data = toByteArray();
        return new DatagramPacket(data, data.length, address, DiscoveryService.PORT);
    }

    public static Packet fromByteArray(byte[] data) {
        Packet result = new Packet();
        result.setType(data[32]);
        result.setTargetMac(ArrayUtils.subarray(data, 8, 14));
        result.setGatewayMac(ArrayUtils.subarray(data, 16, 22));
        result.setTimestamp(ArrayUtils.subarray(data, 24, 32));
        if (data.length > 36) {
            result.setPayload(ArrayUtils.subarray(data, 36, data.length));
        }
        return result;
    }

}
