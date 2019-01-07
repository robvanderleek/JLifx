package io.github.robvanderleek.jlifx.common;

import org.apache.commons.lang3.ArrayUtils;

public class BitField {
    private final boolean[] bits;

    public BitField(int nbits) {
        bits = new boolean[nbits];
    }

    public BitField concat(BitField bitField) {
        BitField result = new BitField(getNumberOfBits() + bitField.getNumberOfBits());
        int i = 0;
        for (int j = 0; j < getNumberOfBits(); j++) {
            result.set(i, get(j));
            i++;
        }
        for (int j = 0; j < bitField.getNumberOfBits(); j++) {
            result.set(i, bitField.get(j));
            i++;
        }
        return result;
    }

    boolean get(int index) {
        if (index > bits.length - 1) {
            return false;
        }
        return bits[index];
    }

    public void set(int index, boolean b) {
        bits[index] = b;
    }

    public byte[] toByteArray() {
        int nrOfBytes = (int) Math.ceil(getNumberOfBits() / 8f);
        byte[] result = new byte[nrOfBytes];
        for (int i = 0; i < nrOfBytes; i++) {
            byte b = 0;
            b |= get(i * 8) ? 0x80 : 0x00;
            b |= get(i * 8 + 1) ? 0x40 : 0x00;
            b |= get(i * 8 + 2) ? 0x20 : 0x00;
            b |= get(i * 8 + 3) ? 0x10 : 0x00;
            b |= get(i * 8 + 4) ? 0x08 : 0x00;
            b |= get(i * 8 + 5) ? 0x04 : 0x00;
            b |= get(i * 8 + 6) ? 0x02 : 0x00;
            b |= get(i * 8 + 7) ? 0x01 : 0x00;
            result[i] = b;
        }
        ArrayUtils.reverse(result);
        return result;
    }

    int getNumberOfBits() {
        return bits.length;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (boolean bit : bits) {
            result.append(bit ? "1" : "0");
        }
        return result.toString();
    }
}
