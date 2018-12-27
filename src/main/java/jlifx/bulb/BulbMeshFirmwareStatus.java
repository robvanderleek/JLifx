package jlifx.bulb;

import jlifx.commandline.Utils;
import jlifx.packet.Packet;

import java.util.Date;

public class BulbMeshFirmwareStatus {
    private final Date buildTimestamp;
    private final Date installTimestamp;
    private final int version;

    private BulbMeshFirmwareStatus(Date buildTimestamp, Date installTimestamp, int version) {
        this.buildTimestamp = buildTimestamp;
        this.installTimestamp = installTimestamp;
        this.version = version;
    }

    public Date getBuildTimestamp() {
        return buildTimestamp;
    }

    public Date getInstallTimestamp() {
        return installTimestamp;
    }

    public int getVersion() {
        return version;
    }

    public static BulbMeshFirmwareStatus fromPacket(Packet packet) {
        byte[] payload = packet.getPayload();
        // 8 byte first date
        // 8 byte second date
        int version = Utils.from32bitsLittleEndian(payload[16], payload[17], payload[18], payload[19]);
        return new BulbMeshFirmwareStatus(new Date(), new Date(), version);
    }
}