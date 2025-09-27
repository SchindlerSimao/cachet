package ch.heigvd;

import java.security.PrivateKey;
import java.security.PublicKey;

public class SignatureOperations {
    public static String sign(final byte[] data, final PrivateKey privateKey) {
        return "signature";
    }

    public static boolean verify(final String data, final String signature, final PublicKey publicKeyPath) {
        return true;
    }
}
