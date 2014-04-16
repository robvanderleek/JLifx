package jlifx.packet;

public class StatusResponsePacket {
    private final Packet packet;

    public StatusResponsePacket(Packet packet) {
        this.packet = packet;
    }

    public byte[] getType() {
        return packet.getType();
    }

    public byte[] getTargetMac() {
        return packet.getTargetMac();
    }

    public int getHue() {
        return ((packet.getPayload()[1] << 8) | packet.getPayload()[0]) & 0xFFFF;
    }

    public int getSaturation() {
        return ((packet.getPayload()[3] << 8) | packet.getPayload()[2]) & 0xFFFF;
    }

    public int getBrightness() {
        return ((packet.getPayload()[5] << 8) | packet.getPayload()[4]) & 0xFFFF;
    }

    public int getKelvin() {
        return ((packet.getPayload()[7] << 8) | packet.getPayload()[6]) & 0xFFFF;
    }

    public int getDim() {
        return ((packet.getPayload()[9] << 8) | packet.getPayload()[8]) & 0xFFFF;
    }

    public int getPower() {
        return ((packet.getPayload()[10] << 8) | packet.getPayload()[11]) & 0xFFFF;
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