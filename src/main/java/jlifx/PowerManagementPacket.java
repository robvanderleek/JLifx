package jlifx;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PowerManagementPacket extends Packet {
    private static final Log LOG = LogFactory.getLog(PowerManagementPacket.class);
    private final boolean on;

    public PowerManagementPacket(byte[] targetMacAddress, boolean on) {
        super(targetMacAddress, new byte[] {0x15, 0x00});
        this.on = on;
    }

    @Override
    public byte[] toByteArray() {
        byte[] result = new byte[] {};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(super.toByteArray());
            outputStream.write(on ? 0x01 : 0x00);
            outputStream.write(0x00);
            result = outputStream.toByteArray();
            outputStream.close();
            result[0] = 0x26;
        } catch (IOException e) {
            LOG.error("Error building packet");
        }
        return result;
    }
}
