package com.at.archistar.crypto.mac;

import com.at.archistar.crypto.math.gf256.GF256PolyHelper;

import java.security.InvalidKeyException;
import java.util.Arrays;

/**
 * a very simple polynomial hash implementation for cevallos
 */
public class PolyHash implements MacHelper {

    private final int keylength;
    
    /**
     * create a new hash
     *
     * @param keylength output keylength
     */
    public PolyHash(int keylength) {
        this.keylength = keylength;
    }

    private static int[] createIntArrayFromByte(byte[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[1] & 0xff;
        }
        return b;
    }

    /**
     * Compute macs for the given data
     *
     * @param data for which data do we need the mac?
     * @param key the key used for the macd tag that should be compared
     * @return mac for data (with key)
     * @throws InvalidKeyException
     */
    @Override
    public byte[] computeMAC(byte[] data, byte[] key) throws InvalidKeyException {

        assert (this.keylength * 2 == key.length);

        int b[] = createIntArrayFromByte(Arrays.copyOf(key, this.keylength));

        /* set a to be the other part of the key */
        int[] a = createIntArrayFromByte(Arrays.copyOfRange(key, this.keylength, key.length));

        /* set result's bits to the first keylength elements */
        int[] result = createIntArrayFromByte(Arrays.copyOf(data, this.keylength));

        int rowCount = data.length / this.keylength;

        /* reihenfolge innerhalb des arrays sollte ja wurscht sein */
        for (int i = 1; i < rowCount; i++) {
            int[] next = createIntArrayFromByte(Arrays.copyOfRange(data, i * this.keylength, (i + 1) * this.keylength));
            result = GF256PolyHelper.multiply(result, GF256PolyHelper.add(b, next));
        }

        /* add rest -> reihenfolge wurscht, sollt also funktionieren */
        if (data.length % this.keylength != 0) {
            int[] next = createIntArrayFromByte(Arrays.copyOfRange(data, rowCount * this.keylength, data.length));

            /* expand to keylength */
            next = Arrays.copyOf(next, keylength);
            result = GF256PolyHelper.add(result, next);
        }

        /* add a */
        result = GF256PolyHelper.add(result, a);

        /* extract result into a byte[] array */
        byte[] byteResult = new byte[this.keylength];
        for (int i = 0; i < result.length; i++) {
            byteResult[i] = (byte) result[i];
        }
        return byteResult;
    }

    /**
     * Verify mac for the given data
     *
     * @param data for which data do we need the mac?
     * @param key the key used for the mac
     * @param tag the compute
     * @return true if it matches
     */
    @Override
    public boolean verifyMAC(byte[] data, byte[] tag, byte[] key) {
        boolean valid = false;

        try {
            byte[] newTag = computeMAC(data, key); // compute tag for the given parameters
            valid = Arrays.equals(tag, newTag); // compare with original tag
        } finally {
            return valid;
        }
    }

    /**
     * the size of the input key (in bytes) needed for the MAC. We could also
     * add an RandomSource to the class but I'm not too sure which way would be
     * more maintainable.
     *
     * @return the needed input key size
     */
    @Override
    public int keySize() {
        // a and b
        return this.keylength * 2;
    }
}
