package io.github.robvanderleek.jlifx.packet;

import io.github.robvanderleek.jlifx.commandline.Utils;

public class GroupResponsePacket extends Packet {
    public static final byte TYPE = 0x35;

    GroupResponsePacket(Packet p) {
        super(p.getTargetMac(), p.getType());
        setPayload(p.getPayload());
    }

    public byte[] getId(){
        byte[] groupId = new byte[16];
        byte[] payload = getPayload();
        for (int i = 0; i < 16; i++) {
            groupId[i] = (byte) (payload[i] & 0xFF);
        }
        return groupId;
    }

    public String getLabel() {
        StringBuilder result = new StringBuilder();
        byte[] payload = getPayload();
        for (int i = 16; i < 48; i++) {
            result.append((char) (payload[i] & 0xFF));
        }
        return result.toString();
    }

    public long getUpdatedAt() {
        byte[] updatedAtBytes = new byte[64];
        byte[] payload = getPayload();
        for (int i = 48; i < 112; i++) {
            updatedAtBytes[i - 48] = (byte) (payload[i] & 0xFF);
        }
        return Utils.from64bitsLittleEndian(updatedAtBytes);
    }

}