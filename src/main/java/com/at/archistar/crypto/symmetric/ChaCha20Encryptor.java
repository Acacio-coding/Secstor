package com.at.archistar.crypto.symmetric;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Security;

/**
 * basic ChaCha20-based encryption
 *
 * TODO: we are currently using a fixed IV!
 */
public class ChaCha20Encryptor implements Encryptor {

    private final byte[] randomIvBytes = {0, 1, 2, 3, 4, 5, 6, 7};

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] randomKeyBytes) throws IOException, InvalidKeyException,
            InvalidAlgorithmParameterException, InvalidCipherTextException {

        ChaChaEngine cipher = new ChaChaEngine();
        cipher.init(true, new ParametersWithIV(new KeyParameter(randomKeyBytes), randomIvBytes));

        byte[] result = new byte[data.length];
        cipher.processBytes(data, 0, data.length, result, 0);
        return result;
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] randomKeyBytes)
            throws InvalidKeyException, InvalidAlgorithmParameterException, IOException,
            IllegalStateException, InvalidCipherTextException {

        ChaChaEngine cipher = new ChaChaEngine();
        cipher.init(false, new ParametersWithIV(new KeyParameter(randomKeyBytes), randomIvBytes));

        byte[] result = new byte[data.length];
        cipher.processBytes(data, 0, data.length, result, 0);
        return result;
    }

    /**
     * Special method to decrypt partial data
     *
     * @param data to decrypt
     * @param randomKeyBytes key to use
     * @param startingByte the starting position within the (complete) data
     * @return decrypted data
     */
    public byte[] decrypt(byte[] data, byte[] randomKeyBytes, long startingByte) {

        ChaChaEngine cipher = new ChaChaEngine();
        cipher.init(false, new ParametersWithIV(new KeyParameter(randomKeyBytes), randomIvBytes));
        cipher.skip(startingByte);
        byte[] result = new byte[data.length];
        cipher.processBytes(data, 0, data.length, result, 0);
        return result;
    }

    @Override
    public int getKeyLength() {
        return 32;
    }

    @Override
    public String toString() {
        return "ChaCha20()";
    }
}
