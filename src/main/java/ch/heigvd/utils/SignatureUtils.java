package ch.heigvd.utils;

import ch.heigvd.SignatureConstants;

import java.security.*;

/**
 * This class provides methods for signing data and verifying signatures.
 */
public class SignatureUtils {
    /**
     * Signs the given data with the provided private key.
     * @param data data to sign
     * @param privateKey private key to use for signing
     * @return the signature as a byte array
     */
    public static byte[] sign(final byte[] data, final PrivateKey privateKey) {
        try{
            final Signature ecdsaSign = Signature.getInstance(SignatureConstants.SIGNATURE_ALGORITHM);
            ecdsaSign.initSign(privateKey);
            ecdsaSign.update(data);

            return ecdsaSign.sign();
        } catch (final SignatureException exception){
            System.err.printf("Erreur de signature %s%n", exception.getMessage());
            throw new RuntimeException(exception); // TODO : custom exception
        } catch (InvalidKeyException e) {
            System.err.println("Clé invalide");
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithme non supporté");
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies the given signature against the data using the provided public key.
     * @param data data to verify
     * @param signature signature to verify
     * @param publicKey public key to use for verification
     * @return true if the signature is valid, false otherwise
     */
    public static boolean verify(final byte[] data, final byte[] signature, final PublicKey publicKey) {
        try {
            final Signature verifier = Signature.getInstance(SignatureConstants.SIGNATURE_ALGORITHM);
            verifier.initVerify(publicKey);
            verifier.update(data);

            return verifier.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e); // TODO: qqch de plus propre
        }
    }

    public static KeyPair generateKeyPair() {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SignatureConstants.SIGNATURE_ALGORITHM);

            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
