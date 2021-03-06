package com.at.archistar.crypto.secretsharing;

import com.at.archistar.crypto.data.InvalidParametersException;
import com.at.archistar.crypto.data.ShamirShare;
import com.at.archistar.crypto.data.Share;
import com.at.archistar.crypto.decode.Decoder;
import com.at.archistar.crypto.decode.DecoderFactory;
import com.at.archistar.crypto.decode.UnsolvableException;
import com.at.archistar.crypto.math.gf256.GF256;
import com.at.archistar.crypto.random.RandomSource;

/**
 * <p>This class implements the Perfect-Secret-Sharing-scheme (PSS) developed by Adi Shamir.</p>
 *
 * <p>This implementation utilizes GeometricSecretSharing to obtain most of this
 * algorithm's implementation -- it's main addition is the encoding/decoding of
 * secrets into the equations coefficients ([a_0 .. a_k] in GeometricSecretSharing's
 * documentation). Shamir just sets one coefficient (a_0 in our implementation)
 * to the secret and sets all other coefficients to random data. Compared to other
 * schemes this yields security but has the payoff of being rather slow.</p>
 *
 * <p>For a detailed description of the scheme,
 * see: <a href='http://en.wikipedia.org/wiki/Shamir's_Secret_Sharing'>http://en.wikipedia.org/wiki/Shamir's_Secret_Sharing</a></p>
 */
public class ShamirPSS extends GeometricSecretSharing {

    private final RandomSource rng;
    private final byte[] rand;

    /**
     * Constructor
     *
     * @param n the number of shares to create
     * @param k the minimum number of shares required for reconstruction
     * @param rng the source of randomness to use for generating the coefficients
     * @param decoderFactory the solving algorithm to use for reconstructing the secret
     * @throws WeakSecurityException thrown if this scheme is not secure enough for the given parameters
     */
    public ShamirPSS(int n, int k, RandomSource rng, DecoderFactory decoderFactory) throws WeakSecurityException {
        super(n, k, decoderFactory);
        this.rng = rng;
        this.rand = new byte[k - 1];
    }

    @Override
    public String toString() {
        return "ShamirPSS(" + n + "/" + k + ")";
    }

    @Override
    protected int decodeData(int[] encoded, int originalLength, byte[] result, int offset) {
        result[offset++] = (byte) encoded[0];
        return offset;
    }

    @Override
    protected ShamirShare[] createShares(int[] xValues, byte[][] results, int originalLength) throws InvalidParametersException {
        ShamirShare[] shares = new ShamirShare[n];

        for (int i = 0; i < n; i++) {
            shares[i] = new ShamirShare((byte) xValues[i], results[i]);
        }

        return shares;
    }

    @Override
    protected int encodedSizeFor(int length) {
        return length;
    }

    @Override
    public void share(byte[][] output, byte[] data) {
        for (int i = 0; i < data.length; i++) {
            rng.fillBytes(rand);
            for (int j = 0; j < n; j++) {
                int res = rand[0] & 0xff;
                for (int y = 1; y < k - 1; y++) {
                    res = GF256.add(rand[y] & 0xff, mulTables[j][res]);
                }
                output[j][i] = (byte) GF256.add(data[i] & 0xff, mulTables[j][res]);
            }
        }
    }

    @Override
    public ShamirShare[] recover(Share[] shares) throws ReconstructionException {
        byte[] missing = determineMissingShares(shares);
        int len = shares[0].getYValues().length;
        ShamirShare[] res = new ShamirShare[missing.length];
        for (int i = 0; i < missing.length; i++) {
            try {
                res[i] = new ShamirShare(missing[i], new byte[len]);
            } catch (InvalidParametersException e) {
                throw new ReconstructionException("This should not have happened");
            }
        }
        Decoder decoder = decoderFactory.createDecoder(extractXVals(shares, k), k);
        final int[] coeffs = new int[k];
        final int[] temp = new int[k];

        for (int i = 0; i < len; i++) {
            for (int x = 0; x < k; x++) {
                coeffs[x] = shares[x].getYValues()[i] & 0xff;
            }
            try {
                decoder.decodeUnsafe(temp, coeffs, 0);
            } catch (UnsolvableException e) {
                throw new ReconstructionException(e.toString());
            }
            for (int j = 0; j < missing.length; j++) {
                res[j].getYValues()[i] = (byte) GF256.evaluateAt(temp, missing[j]);
            }
        }

        return res;
    }
}
