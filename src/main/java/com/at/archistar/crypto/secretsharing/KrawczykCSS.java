package com.at.archistar.crypto.secretsharing;

import com.at.archistar.crypto.data.*;
import com.at.archistar.crypto.decode.DecoderFactory;
import com.at.archistar.crypto.random.RandomSource;
import com.at.archistar.crypto.symmetric.AESEncryptor;
import com.at.archistar.crypto.symmetric.AESGCMEncryptor;
import com.at.archistar.crypto.symmetric.ChaCha20Encryptor;
import com.at.archistar.crypto.symmetric.Encryptor;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * <p>This class implements the Computational Secret Sharing scheme developed by Krawczyk.</p>
 *
 * <p>This is a hybrid scheme that combines classical symmetric encryption, ShamirPSS
 * and RabinIDS. The secret data is initially encrypted using an traditional
 * symmetric encryption scheme (i.e. AES, Salsa, ChaCha..). The resulting encoded
 * data is then distributed between participants with the fast but insecure
 * RabinIDS scheme. The key that was used during the encryption is split up using
 * the slow but secure ShamirPSS scheme and also distributed between participants.
 * The performance benefit arises from the fact that the encryption key is many
 * times smaller then the encrypted data -- the (computation- and storage-wise)
 * inefficient shamir-pss algorithm thus performs only on small amounts of data
 * while the faster performing and more space-efficient rabin-ids algorithm is
 * used for the encrypted (larger) data.</p>
 *
 * <p>For detailed information about this scheme, see:
 * http://courses.csail.mit.edu/6.857/2009/handouts/short-krawczyk.pdf</p>
 */
public class KrawczykCSS extends BaseSecretSharing {

    private final RandomSource rng;

    private final ShamirPSS shamir;

    private final RabinIDS rs;

    private final Encryptor cryptor;

    private final byte[] additionalKey;

    /**
     * Krawczyk
     *
     * @param n the number of shares
     * @param k the minimum number of shares required for reconstruction
     * @param rng the RandomSource to be used for the underlying Shamir-scheme
     * @param cryptor the to be used encryption algorithms
     * @param decFactory the decoder
     * @param additionalKey if present, generated keys will be encrypted with this key
     * @throws WeakSecurityException thrown if this scheme is not secure for the given parameters
     * @throws InvalidParametersException when the length of the additional key is wrong
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public KrawczykCSS(int n, int k, RandomSource rng, Encryptor cryptor,
                       DecoderFactory decFactory, byte[] additionalKey) throws WeakSecurityException, InvalidParametersException {
        super(n, k);
        if (additionalKey != null && additionalKey.length != cryptor.getKeyLength()) {
            throw new InvalidParametersException("Key has length " + additionalKey.length + " but needs to have length " + cryptor.getKeyLength());
        }
        this.shamir = new ShamirPSS(n, k, rng, decFactory);
        this.rs = new RabinIDS(n, k, decFactory);
        this.cryptor = cryptor;
        this.rng = rng;
        this.additionalKey = additionalKey;
    }

    public KrawczykCSS(int n, int k, RandomSource rng, Encryptor cryptor,
                       DecoderFactory decFactory) throws WeakSecurityException {
        super(n, k);
        this.shamir = new ShamirPSS(n, k, rng, decFactory);
        this.rs = new RabinIDS(n, k, decFactory);
        this.cryptor = cryptor;
        this.rng = rng;
        this.additionalKey = null;
    }

    @Override
    public KrawczykShare[] share(byte[] data) {
        try {
            if (data == null) {
                data = new byte[0];
            }
            /* encrypt the data */
            byte[] encKey = new byte[cryptor.getKeyLength()];
            this.rng.fillBytes(encKey);
            byte[] encSource = cryptor.encrypt(data, encKey);
            if (additionalKey != null) {
                encKey = cryptor.encrypt(encKey, additionalKey);
            }

            int baseDataLength = data.length;
            // block cyphers use 16 byte blocks and add an extra block at the end
            if (cryptor instanceof AESEncryptor || cryptor instanceof AESGCMEncryptor) {
                baseDataLength = ((data.length / 16) + 1) * 16;
            }
            int newDataLength = baseDataLength % k == 0 ? baseDataLength / k : (baseDataLength / k) + 1;

            /* share key and content */
            byte[][] outputContent = new byte[n][newDataLength];
            byte[][] outputKey = new byte[n][encKey.length];

            rs.share(outputContent, encSource);
            shamir.share(outputKey, encKey);

            //Generate a new array of encrypted shares
            KrawczykShare[] kshares = new KrawczykShare[n];
            for (int i = 0; i < kshares.length; i++) {
                kshares[i] = new KrawczykShare((byte) (i + 1), outputContent[i], encSource.length, 1, outputKey[i]);

            }

            return kshares;
        } catch (GeneralSecurityException | InvalidCipherTextException | IOException | InvalidParametersException e) {
            // encryption should actually never fail
            throw new RuntimeException("impossible: sharing failed (" + e.getMessage() + ")");
        }
    }

    @SuppressWarnings("cyclomaticcomplexity")
    private byte[] reconstruct(Share[] shares, boolean partial, long start) throws ReconstructionException {

        if (partial && !(cryptor instanceof ChaCha20Encryptor)) {
            throw new ReconstructionException("Partial reconstruction non-compatible cypher attempted");
        }
        if (shares.length < k) {
            throw new ReconstructionException("too few shares");
        }

        for (Share s : shares) {
            if (!(s instanceof KrawczykShare)) {
                throw new ReconstructionException("Not all shares are Krawczyk shares");
            }
        }

        int originalLengthContent = shares[0].getOriginalLength();
        int originalLengthKey = ((KrawczykShare) shares[0]).getKey().length;
        for (Share s : shares) {
            if (s.getOriginalLength() != originalLengthContent) {
                throw new ReconstructionException("Shares have different original length");
            }
            if (((KrawczykShare) s).getKey().length != originalLengthKey) {
                throw new ReconstructionException("Shares have different key length");
            }
        }

        try {
            int[] xValues = GeometricSecretSharing.extractXVals(shares, k);

            byte[][] ecContent = new byte[n][];
            byte[][] ecKey = new byte[n][];

            for(int i = 0; i < k; i++) {
                ecContent[i] = shares[i].getYValues();
                ecKey[i] = ((KrawczykShare) shares[i]).getKey();
            }

            byte[] key = shamir.reconstruct(ecKey, xValues, originalLengthKey);
            if (additionalKey != null) {
                key = cryptor.decrypt(key, additionalKey);
            }
            if (partial) {
                int actualLengthContent = shares[0].getYValues().length;
                for (Share s : shares) {
                    if (s.getYValues().length != actualLengthContent) {
                        throw new ReconstructionException("Shares have different actual length");
                    }
                }
                int reconstructionLength = actualLengthContent * k;
                return ((ChaCha20Encryptor) cryptor).decrypt(rs.reconstruct(ecContent, xValues, reconstructionLength), key, start);
            } else {
                return cryptor.decrypt(rs.reconstruct(ecContent, xValues, originalLengthContent), key);
            }
        } catch (GeneralSecurityException | IOException | IllegalStateException | InvalidCipherTextException e) {
            // decryption should actually never fail
            throw new RuntimeException("impossible: reconstruction failed (" + e.getMessage() + ")");
        }
    }

    @Override
    public byte[] reconstruct(Share[] shares) throws ReconstructionException {
        return reconstruct(shares, false, 0);
    }

    @Override
    public byte[] reconstructPartial(Share[] shares, long start) throws ReconstructionException {
        if (cryptor instanceof AESEncryptor || cryptor instanceof AESGCMEncryptor) {
            throw new ReconstructionException("Partial reconstruction not possible with given cypher");
        }
        return reconstruct(shares, true, start);
    }

    @Override
    public KrawczykShare[] recover(Share[] shares) throws ReconstructionException {
        byte[] missing = determineMissingShares(shares);
        int olen = shares[0].getOriginalLength();
        int algo = ((KrawczykShare) (shares[0])).getEncAlgorithm();

        RabinShare[] rabinShares = new RabinShare[shares.length];
        ShamirShare[] shamirShares = new ShamirShare[shares.length];

        for (int i = 0; i < shares.length; i++) {
            KrawczykShare s = (KrawczykShare) shares[i];
            try {
                rabinShares[i] = new RabinShare(s.getId(), s.getYValues(), s.getOriginalLength());
                shamirShares[i] = new ShamirShare(s.getId(), s.getKey());
            } catch (InvalidParametersException e) {
                throw new ReconstructionException(e.toString());
            }
        }

        RabinShare[] recoveredRabin = rs.recover(rabinShares);
        ShamirShare[] recoveredShamir = shamir.recover(shamirShares);

        KrawczykShare[] res = new KrawczykShare[missing.length];

        for (int i = 0; i < missing.length; i++) {
            try {
                res[i] = new KrawczykShare(missing[i], recoveredRabin[i].getYValues(), olen, algo, recoveredShamir[i].getYValues());
            } catch (InvalidParametersException e) {
                throw new ReconstructionException(e.toString());
            }
        }

        return res;
    }

    @Override
    public String toString() {
        return "KrawczykCSS(" + n + "/" + k + ", " + cryptor + ")";
    }
}
