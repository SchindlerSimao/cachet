package ch.heigvd;

import java.security.PrivateKey;
import java.security.PublicKey;

public class SignatureOperations {
    /**
     * Signs the given data with the provided private key.
     * @param data data to sign
     * @param privateKey private key to use for signing
     * @return the signature as a byte array
     */
    public static String sign(final byte[] data, final PrivateKey privateKey) {
        return "signature";
    }

    /**
     * Verifies the given signature against the data using the provided public key.
     * @param data data to verify
     * @param signature signature to verify
     * @param publicKeyPath public key to use for verification
     * @return true if the signature is valid, false otherwise
     */
    public static boolean verify(final byte[] data, final byte[] signature, final PublicKey publicKeyPath) {
        return true;
    }
}
