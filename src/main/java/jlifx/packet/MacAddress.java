package jlifx.packet;

import jlifx.commandline.Utils;

public class MacAddress {
    public static final MacAddress ALL_BULBS = new MacAddress(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
    private final byte[] address = new byte[6];

    public MacAddress(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6) {
        this(new byte[]{b1, b2, b3, b4, b5, b6});
    }

    public MacAddress(byte[] bytes) {
        if (bytes.length != 6) {
            throw new RuntimeException("MAC address has invalid size");
        }
        System.arraycopy(bytes, 0, address, 0, bytes.length);
    }

    public byte[] toByteArray() {
        byte[] result = new byte[8];
        System.arraycopy(address, 0, result, 0, address.length);
        result[6] = 0x00;
        result[7] = 0x00;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MacAddress)) {
            return false;
        }
        MacAddress that = (MacAddress) obj;
        return that.address[0] == address[0] && that.address[1] == address[1] && that.address[2] == address[2] &&
                that.address[3] == address[3] && that.address[4] == address[4] && that.address[5] == address[5];
    }

    @Override
    public int hashCode() {
        return address[0] + address[1] + address[2] + address[3] + address[4] + address[5];
    }

    @Override
    public String toString() {
        return Utils.getMacAddressAsString(address);
    }
}
