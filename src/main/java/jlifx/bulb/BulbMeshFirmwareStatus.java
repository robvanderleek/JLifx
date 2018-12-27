package jlifx.bulb;

import jlifx.commandline.Utils;
import jlifx.packet.Packet;

import java.util.Date;

public class BulbMeshFirmwareStatus {
    private final Date buildTimestamp;
    private final int version;

    private BulbMeshFirmwareStatus(Date buildTimestamp, int version) {
        this.buildTimestamp = buildTimestamp;
        this.version = version;
    }

    public Date getBuildTimestamp() {
        return buildTimestamp;
    }

    public int getVersion() {
        return version;
    }

    public static BulbMeshFirmwareStatus fromPacket(Packet packet) {
        byte[] payload = packet.getPayload();
        long nanoSecondsSinceEpoch = Utils.from64bitsLittleEndian(payload[0], payload[1], payload[2], payload[3],
                payload[4], payload[5], payload[6], payload[7]);
        Date buildTimestamp = new Date(nanoSecondsSinceEpoch / 1000000);
        int version = Utils.from32bitsLittleEndian(payload[16], payload[17], payload[18], payload[19]);
        return new BulbMeshFirmwareStatus(buildTimestamp, version);
    }
}