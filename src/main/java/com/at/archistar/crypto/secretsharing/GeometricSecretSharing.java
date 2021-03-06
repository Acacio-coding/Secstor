package com.at.archistar.crypto.secretsharing;

import com.at.archistar.crypto.data.InvalidParametersException;
import com.at.archistar.crypto.data.Share;
import com.at.archistar.crypto.decode.Decoder;
import com.at.archistar.crypto.decode.DecoderFactory;
import com.at.archistar.crypto.decode.UnsolvableException;
import com.at.archistar.crypto.math.gf256.GF256;

/**
 * <p>this contains basic functionality utilized by RabinIDS and ShamirPSS.</p>
 *
 * <p>Secret-sharing can be seen as solving an overdetermined Equation. Given an
 * equation of degree k a minimum of k points are needed to solve the equation.
 * I.e. for degree 3 there's an equation y = a0*x^0 + a1*x^1 + a2*x^2. We need
 * 3 (x,y) pairs to determine [a0, a1, a2]. Secret-sharing schemes deriving from
 * this class utilize this: for a k/n sharing scheme we're creating a equation
 * of degree k (so k (x,y) pairs will be needed to solve the equation) and fill
 * in [a0.. a_k] with data. By setting in random x-Values we calculate n (x,y)
 * pairs -- those are the shares that will be distributed between participants.</p>
 */
public abstract class GeometricSecretSharing extends BaseSecretSharing {

    final int[][] mulTables;
    private final int[] xValues;
    final DecoderFactory decoderFactory;

    /**
     * Constructor
     *
     * @param n the number of shares to create
     * @param k the minimum number of shares required for reconstruction
     * @param decoderFactory the solving algorithm to use for reconstructing the secret
     * @throws WeakSecurityException thrown if this scheme is not secure enough for the given parameters
     */
    public GeometricSecretSharing(int n, int k, DecoderFactory decoderFactory) throws WeakSecurityException {
        super(n, k);
        this.decoderFactory = decoderFactory;

        xValues = new int[n];
        mulTables = new int[n][];
        for (int i = 0; i < n; i++) {
            xValues[i] = i + 1;
            mulTables[i] = new int[256];
            for (int j = 0; j < 256; j++) {
                mulTables[i][j] = GF256.mult(i + 1, j);
            }
        }
    }

    /**
     * Extracts k x-values from the given shares.
     *
     * @param shares the shares to extract the x-values from
     * @param k how many xValues do we want?
     * @return an array with all x-values from the given shares (in same order as the given Share[])
     */
    public static int[] extractXVals(Share[] shares, int k) {
        int[] x = new int[k];

        for (int i = 0; i < k; i++) {
            x[i] = shares[i].getId();
        }

        return x;
    }

    /**
     * Creates <i>n</i> secret shares for the given data where <i>k</i> shares are required for reconstruction.
     *
     * @param data the data to share secretly
     * @param output n buffers where the output will be stored
     */
    public abstract void share(byte[][] output, byte[] data);

    /**
     * Creates <i>n</i> secret shares for the given data where <i>k</i> shares
     * are required for reconstruction. (n, k should have been previously initialized)
     *
     * @param data the data to share secretly
     * @return the n different secret shares for the given data
     */
    @Override
    public Share[] share(byte[] data) {
        if (data == null) {
            data = new byte[0];
        }

        try {
            byte[][] output = new byte[n][encodedSizeFor(data.length)];
            share(output, data);

            return createShares(xValues, output, data.length);
        } catch (InvalidParametersException ex) {
            throw new RuntimeException("impossible: share failed: " + ex.getMessage());
        }
    }

    /**
     * After data was encoded algorithm-specific share must be created. As the
     * generic implementation does not know, which fields to fill (or which meta
     * data is needed) this task was delegated to the implementing sub-class.
     *
     * @param xValues the used xValues
     * @param results output buffer with the expected body (of the new share)
     * @param originalLength the original data length (of the secret)
     * @return Array of created shares
     * @throws InvalidParametersException if no valid share was build
     */
    protected abstract Share[] createShares(int[] xValues,
                                            byte[][] results,
                                            int originalLength) throws InvalidParametersException;

    /**
     * Attempts to reconstruct the secret from the given input stream.
     * This will fail if there are fewer than k (previously initialized) valid shares.
     *
     * @param input the body of the share's to reconstruct the secret from
     * @param xValues the xValues (from the shares)
     * @param originalLength the secret's length -- this might be need to recognize padding
     * @return the reconstructed secret
     * @throws ReconstructionException thrown if the reconstruction failed
     */
    public byte[] reconstruct(byte[][] input, int[] xValues, int originalLength) throws ReconstructionException {
        Decoder decoder = decoderFactory.createDecoder(xValues, k);
        byte[] result = new byte[originalLength];
        int[] yValues = new int[k];
        int[] resultMatrix = new int[k];

        int posResult = 0;
        int posInput = 0;

        while (posResult < originalLength) {
            for (int j = 0; j < k; j++) { // extract only k y-values (so we have k xy-pairs)
                yValues[j] = input[j][posInput] & 0xff;
            }
            posInput++;

            /* perform matrix-multiplication to compute the coefficients */
            try {
                decoder.decodeUnsafe(resultMatrix, yValues, 0);
                posResult = decodeData(resultMatrix, originalLength, result, posResult);
            } catch (UnsolvableException e) {
                throw new ReconstructionException(e.getMessage());
            }
        }
        return result;
    }

    /**
     * Attempts to reconstruct the secret from the given shares.<br>
     * This will fail if there are fewer than k (previously initialized) valid shares.
     *
     * @param shares the shares to reconstruct the secret from
     * @return the reconstructed secret
     * @throws ReconstructionException thrown if the reconstruction failed
     */
    @Override
    public byte[] reconstruct(Share[] shares) throws ReconstructionException {
        if (!validateShareCount(shares.length, k)) {
            throw new ReconstructionException("Not enough shares to reconstruct");
        }

        int originalLength = shares[0].getOriginalLength();
        for (Share s : shares) {
            if (s.getOriginalLength() != originalLength) {
                throw new ReconstructionException("Shares have different original length");
            }
        }

        // we only need k x-values for reconstruction
        int[] xTmpValues = extractXVals(shares, k);

        byte[][] tmp = new byte[shares.length][];
        for(int i = 0; i < shares.length; i++) {
            tmp[i] = shares[i].getYValues();
        }

        return reconstruct(tmp, xTmpValues, originalLength);
    }

    /**
     * For this kind of scheme, partial reconstruction is the same as full reconstruction
     *
     * @param shares the partial shares to reconstruct the secret from
     * @param start the starting position relative to the original data (ignored here)
     * @return the partially reconstructed secret
     * @throws ReconstructionException thrown if the reconstruction failed
     */
    @Override
    public byte[] reconstructPartial(Share[] shares, long start) throws ReconstructionException {
        return reconstruct(shares);
    }

    /**
     * While reconstructing the original secret the encrypted data is passed on
     * to an equation solver. The result of this operation is passed on to this
     * method which should extract the original data from the solver's result.
     *
     * @param encoded original data that was put into the equation during sharing
     * @param originalLength how long was the original data. This is needed as
     * some algorithms (i.e. RabinIDS) utilize padding if
     * the last data block wasn't fully filled.
     * @param result an array where the resulting data should be stored in. The
     * concrete position within this array is given by offset
     * @param offset current position within result array
     * @return amount (in byte) of retrieved original data (used to calculate
     * new offset)
     */
    protected abstract int decodeData(int[] encoded, int originalLength, byte[] result, int offset);

    protected abstract int encodedSizeFor(int length);
}
