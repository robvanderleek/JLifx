package io.github.robvanderleek.jlifx.packet;

public class StatusResponsePacket extends Packet {

    StatusResponsePacket(Packet p) {
        super(p.getTargetMac(), p.getType());
        setPayload(p.getPayload());
    }

    public int getHue() {
        return ((getPayload()[1] << 8) | getPayload()[0]) & 0xFFFF;
    }

    public int getSaturation() {
        return ((getPayload()[3] << 8) | getPayload()[2]) & 0xFFFF;
    }

    public int getBrightness() {
        return ((getPayload()[5] << 8) | getPayload()[4]) & 0xFFFF;
    }

    public int getKelvin() {
        return ((getPayload()[7] << 8) | getPayload()[6]) & 0xFFFF;
    }

    public int getDim() {
        return ((getPayload()[9] << 8) | getPayload()[8]) & 0xFFFF;
    }

    public int getPower() {
        return ((getPayload()[10] << 8) | getPayload()[11]) & 0xFFFF;
    }

    public String getBulbName() {
        StringBuilder result = new StringBuilder();
        byte[] payload = getPayload();
        if (payload.length > 12) {
            for (int i = 12; i < payload.length; i++) {
                if (payload[i] == 0x00) break;
                result.append((char) (payload[i] & 0xFF));
            }
        }
        return result.toString();
    }

}