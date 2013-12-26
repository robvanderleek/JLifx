package jlifx.packet;

public class StatusResponsePacket {
    private final Packet packet;

    public StatusResponsePacket(Packet packet) {
        this.packet = packet;
    }

    public byte[] getTargetMac() {
        return packet.getTargetMac();
    }

    public int getHue() {
        return (packet.getPayload()[1] << 8) | packet.getPayload()[0];
    }

    public int getSaturation() {
        return (packet.getPayload()[3] << 8) | packet.getPayload()[2];
    }

    public String getBulbName() {
        StringBuffer result = new StringBuffer();
        byte[] payload = packet.getPayload();
        if (payload.length > 12) {
            for (int i = 12; i < payload.length; i++) {
                if (payload[i] == 0x00)
                    break;
                result.append((char)(payload[i] & 0xFF));
            }
        }
        return result.toString();
    }

}