package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.commandline.Utils;
import io.github.robvanderleek.jlifx.packet.Packet;
import org.apache.commons.lang3.ArrayUtils;

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
        long nanoSecondsSinceEpoch = Utils.from64bitsLittleEndian(ArrayUtils.subarray(payload, 0, 8));
        Date buildTimestamp = new Date(nanoSecondsSinceEpoch / 1000000);
        int version = Utils.from32bitsLittleEndian(payload[16], payload[17], payload[18], payload[19]);
        return new BulbMeshFirmwareStatus(buildTimestamp, version);
    }
}