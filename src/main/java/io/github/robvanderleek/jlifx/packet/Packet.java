package io.github.robvanderleek.jlifx.packet;

import io.github.robvanderleek.jlifx.bulb.DiscoveryService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;


public class Packet {
    private static final Log LOG = LogFactory.getLog(Packet.class);
    private static final byte[] JLIFX_CLIENT_ID = new byte[]{0x0B, 0x0E, 0x0E, 0x0F};
    private final BitField origin = new BitField(2);
    private final BitField tagged = new BitField(1);
    private final BitField addressable = new BitField(1);
    private final BitField protocol = new BitField(12);

    {
        tagged.set(0, true);
        addressable.set(0, true);
        protocol.set(1, true);
    }

    private final byte[] clientID = JLIFX_CLIENT_ID;
    private MacAddress targetMac = MacAddress.ALL_BULBS;
    private final byte[] reserved1 = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private final BitField reserved2 = new BitField(6);
    private final BitField ackRequired = new BitField(1);
    private final BitField resRequired = new BitField(1);
    private byte sequenceNumber = 0x00;
    private final byte[] reserved3 = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private byte[] type = new byte[]{0x02, 0x00};
    private final byte[] reserved4 = new byte[]{0x00, 0x00};
    private byte[] payload = new byte[]{};

    public Packet() {
    }

    public Packet(MacAddress targetMac, byte type) {
        setTargetMac(targetMac);
        setType(type);
    }

    public Packet(Packet packet) {
        setTargetMac(packet.getTargetMac());
        setType(packet.getType());
        setSequenceNumber(packet.getSequenceNumber());
        setPayload(packet.getPayload());
    }

    public MacAddress getTargetMac() {
        return targetMac;
    }

    public void setTargetMac(MacAddress targetMac) {
        if (targetMac.equals(MacAddress.ALL_BULBS)) {
            this.tagged.set(0, true);
        } else {
            this.tagged.set(0, false);
        }
        this.targetMac = targetMac;
    }

    public void setAckRequired(boolean b) {
        ackRequired.set(0, b);
    }

    public void setResponseRequired(boolean b) {
        resRequired.set(0, b);
    }

    public byte getType() {
        return type[0];
    }

    public byte getResponseType() {
        return (byte) (getType() + 1);
    }

    void setType(byte type) {
        this.type = new byte[]{type, 0x00};
    }

    public byte[] getPayload() {
        return payload;
    }

    void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(byte sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(new byte[]{(byte) (0x24 + payload.length), 0x00});
            outputStream.write(origin.concat(tagged).concat(addressable).concat(protocol).toByteArray());
            outputStream.write(clientID);
            outputStream.write(targetMac.toByteArray());
            outputStream.write(reserved1);
            outputStream.write(reserved2.concat(ackRequired).concat(resRequired).toByteArray());
            outputStream.write(sequenceNumber);
            outputStream.write(reserved3);
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
        return new DatagramPacket(data, data.length, address, DiscoveryService.DISCOVERY_SERVICE_PORT);
    }

    static Packet fromByteArray(byte[] data) {
        Packet result = new Packet();
        result.setType(data[32]);
        result.setTargetMac(new MacAddress(ArrayUtils.subarray(data, 8, 14)));
        result.setSequenceNumber(data[23]);
        if (data.length > 36) {
            result.setPayload(ArrayUtils.subarray(data, 36, data.length));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        byte[] bytes = toByteArray();
        result.append("MAC address: ").append(targetMac).append(", ");
        result.append("message type: ").append(getType() & 0xFF).append(", ");
        result.append("sequence number: ").append(sequenceNumber & 0xFF);
        return result.toString();
    }
}