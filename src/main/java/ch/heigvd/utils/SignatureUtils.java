package ch.heigvd.utils;

import ch.heigvd.Constants;
import ch.heigvd.exceptions.SignatureOperationException;
import ch.heigvd.exceptions.KeyOperationException;

import java.security.*;

/**
 * This class provides methods for signing data and verifying signatures.
 */
public final class SignatureUtils {
    /**
     * Private constructor to prevent instantiation.
     */
    private SignatureUtils() {}

    /**
     * Signs the given data with the provided private key.
     *
     * @param data       data to sign
     * @param privateKey private key to use for signing
     * @return the signature as a byte array
     * @throws SignatureOperationException if signing fails
     * @throws IllegalArgumentException    if data or privateKey is null
     */
    public static byte[] sign(final byte[] data, final PrivateKey privateKey) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Les données à signer ne peuvent pas être nulles ou vides");
        }
        if (privateKey == null) {
            throw new IllegalArgumentException("La clé privée ne peut pas être nulle");
        }

        try {
            final Signature ecdsaSign = Signature.getInstance(Constants.SIGNATURE_ALGORITHM);
            ecdsaSign.initSign(privateKey);
            ecdsaSign.update(data);

            return ecdsaSign.sign();
        } catch (final SignatureException exception) {
            throw new SignatureOperationException("Erreur lors de la signature : " + exception.getMessage(), exception);
        } catch (InvalidKeyException e) {
            throw new KeyOperationException("La clé privée fournie est invalide ou incompatible avec l'algorithme " +
                    Constants.SIGNATURE_ALGORITHM, e);
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureOperationException("L'algorithme de signature " +
                    Constants.SIGNATURE_ALGORITHM + " n'est pas supporté par votre JVM", e);
        }
    }

    /**
     * Verifies the given signature against the data using the provided public key.
     *
     * @param data      data to verify
     * @param signature signature to verify
     * @param publicKey public key to use for verification
     * @return true if the signature is valid, false otherwise
     * @throws SignatureOperationException if verification process fails
     * @throws IllegalArgumentException    if any parameter is null
     */
    public static boolean verify(final byte[] data, final byte[] signature, final PublicKey publicKey) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Les données à vérifier ne peuvent pas être nulles ou vides");
        }
        if (signature == null || signature.length == 0) {
            throw new IllegalArgumentException("La signature ne peut pas être nulle ou vide");
        }
        if (publicKey == null) {
            throw new IllegalArgumentException("La clé publique ne peut pas être nulle");
        }

        try {
            final Signature verifier = Signature.getInstance(Constants.SIGNATURE_ALGORITHM);
            verifier.initVerify(publicKey);
            verifier.update(data);

            return verifier.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureOperationException("L'algorithme de vérification " +
                    Constants.SIGNATURE_ALGORITHM + " n'est pas supporté par votre JVM", e);
        } catch (InvalidKeyException e) {
            throw new KeyOperationException("La clé publique fournie est invalide ou incompatible avec l'algorithme " +
                    Constants.SIGNATURE_ALGORITHM, e);
        } catch (SignatureException e) {
            throw new SignatureOperationException("Erreur lors de la vérification de la signature : " +
                    e.getMessage(), e);
        }
    }
}
