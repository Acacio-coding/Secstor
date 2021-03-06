package com.at.archistar.crypto.secretsharing;

/**
 * This exception is thrown when the given parameters for sharing a secret are
 * not secure enough.
 */
public class WeakSecurityException extends Exception {

    /**
     * create a new exception that denotes that an algorithm was configured
     * in such a way, that it would yield no security or privacy
     *
     * @param msg an detailed error message
     */
    public WeakSecurityException(String msg) {
        super(msg);
    }
}
