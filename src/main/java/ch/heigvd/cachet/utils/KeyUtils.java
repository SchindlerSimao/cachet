package ch.heigvd.cachet.utils;

import ch.heigvd.cachet.Constants;
import ch.heigvd.cachet.exceptions.KeyOperationException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Utility class for loading and saving keys in PEM format.
 */
public final class KeyUtils {
    /**
     * Private constructor to prevent instantiation.
     */
    private KeyUtils() {}

    /**
     * Generates a new key pair for signing and verification.
     *
     * @return a new KeyPair
     * @throws KeyOperationException if key generation fails
     */
    public static KeyPair generateKeyPair() {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Constants.SIGNATURE_ALGORITHM);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new KeyOperationException("L'algorithme " + Constants.SIGNATURE_ALGORITHM +
                    " n'est pas supporté pour la génération de clés", e);
        }
    }

    /**
     * Loads a public key from a file.
     *
     * @param filepath the path to the file containing the public key
     * @return the public key
     * @throws KeyOperationException if the key cannot be loaded
     */
    public static PublicKey loadPublicKey(final String filepath) {
        return (PublicKey) loadKey(filepath, true);
    }

    /**
     * Loads a private key from a file.
     *
     * @param filepath the path to the file containing the private key
     * @return the private key
     * @throws KeyOperationException if the key cannot be loaded
     */
    public static PrivateKey loadPrivateKey(final String filepath) {
        return (PrivateKey) loadKey(filepath, false);
    }

    /**
     * Writes a private key to a file in PEM format.
     *
     * @param filePath   the path to the file where the private key will be written
     * @param privateKey the private key to write
     */
    public static void writePrivateKey(final String filePath, final PrivateKey privateKey) {
        if (privateKey == null) {
            throw new KeyOperationException("La clé privée ne peut pas être nulle");
        }
        writeKey(filePath, privateKey, false);
    }

    /**
     * Writes a public key to a file in PEM format.
     *
     * @param filePath  the path to the file where the public key will be written
     * @param publicKey the public key to write
     */
    public static void writePublicKey(final String filePath, final PublicKey publicKey) {
        if (publicKey == null) {
            throw new KeyOperationException("La clé publique ne peut pas être nulle");
        }
        writeKey(filePath, publicKey, true);
    }

    /**
     * Loads a key (public or private) from a file.
     *
     * @param filepath  the path to the file containing the key
     * @param publicKey true if the key is a public key, false if it is a private key
     * @return the key
     * @throws KeyOperationException if the key cannot be loaded
     */
    private static Key loadKey(final String filepath, final boolean publicKey) {
        final String keyContent = FileIOUtils.fileToString(filepath);
        if (publicKey && !keyContent.contains("BEGIN PUBLIC KEY")) {
            throw new KeyOperationException("Le fichier ne contient pas une clé publique valide au format PEM : " + filepath);
        }
        if (!publicKey && !keyContent.contains("BEGIN PRIVATE KEY")) {
            throw new KeyOperationException("Le fichier ne contient pas une clé privée valide au format PEM : " + filepath);
        }
        final String key = removePemHeaders(keyContent);
        if (key.isEmpty()) {
            throw new KeyOperationException("Le contenu de la clé est vide après suppression des en-têtes PEM : " + filepath);
        }
        final byte[] decodedKey;
        try {
            decodedKey = Base64.getDecoder().decode(key);
        } catch (IllegalArgumentException e) {
            throw new KeyOperationException("Le format Base64 de la clé est invalide : " + filepath, e);
        }
        try {
            final KeyFactory kf = KeyFactory.getInstance(Constants.SIGNATURE_ALGORITHM);
            if (publicKey) {
                final X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
                return kf.generatePublic(spec);
            } else {
                final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
                return kf.generatePrivate(spec);
            }
        } catch (final InvalidKeySpecException e) {
            throw new KeyOperationException(
                    "La spécification de la clé est invalide. Vérifiez que le fichier contient une clé " +
                            (publicKey ? "publique" : "privée") + " valide au format " +
                            (publicKey ? "X.509" : "PKCS#8") + " : " + filepath, e);
        } catch (final NoSuchAlgorithmException e) {
            throw new KeyOperationException("L'algorithme " + Constants.SIGNATURE_ALGORITHM + " n'est pas supporté", e);
        }
    }

    /**
     * Writes a key (public or private) to a file in PEM format.
     *
     * @param filePath  the path to the file where the key will be written
     * @param key       the key to write
     * @param publicKey true if the key is a public key, false if it is a private key
     */
    private static void writeKey(final String filePath, final Key key, final boolean publicKey) {
        FileIOUtils.validateFilePath(filePath);
        FileIOUtils.validateWritableDirectory(filePath);
        final String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        final String pemKey = addPemHeaders(base64Key, publicKey);
        FileIOUtils.writeToFile(pemKey, filePath);
    }

    /**
     * Removes PEM headers and footers from the given PEM content.
     *
     * @param pemContent the PEM content as a String
     * @return the PEM content without headers and footers
     */
    private static String removePemHeaders(final String pemContent) {
        return pemContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
    }

    /**
     * Adds PEM headers and footers to a base64-encoded key string.
     *
     * @param base64Key the base64-encoded key string
     * @param publicKey true if the key is a public key, false if it is a private key
     * @return the PEM-formatted key as a String
     */
    private static String addPemHeaders(final String base64Key, final boolean publicKey) {
        StringBuilder pem = new StringBuilder();
        if (publicKey) {
            pem.append("-----BEGIN PUBLIC KEY-----\n");
        } else {
            pem.append("-----BEGIN PRIVATE KEY-----\n");
        }
        int index = 0;
        while (index < base64Key.length()) {
            pem.append(base64Key, index, Math.min(index + 64, base64Key.length()));
            pem.append("\n");
            index += 64;
        }
        if (publicKey) {
            pem.append("-----END PUBLIC KEY-----\n");
        } else {
            pem.append("-----END PRIVATE KEY-----\n");
        }
        return pem.toString();
    }
}
