package ch.heigvd;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Utility class for file input/output operations and key loading.
 */
public class FileIO {
    /**
     * Reads the content of a file and returns it as a String.
     * 
     * @param filePath the path to the file
     * @return the content of the file as a String
     */
    public static String fileToString(final String filePath) {
        try (final InputStream inputStream = new FileInputStream(filePath)) {
            return new String(inputStream.readAllBytes());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error opening file: " + filePath);
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the content of a file and returns it as a byte array.
     * 
     * @param filePath the path to the file
     * @return the content of the file as a byte array
     */
    public static byte[] fileToBytes(final String filePath) {
        try (final InputStream inputStream = new FileInputStream(filePath)) {
            return inputStream.readAllBytes();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);

            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error opening file: " + filePath);

            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a public key from a file.
     * 
     * @param filepath the path to the file containing the public key
     * @return the public key
     */
    public static PublicKey loadPublicKey(final String filepath) {
        return (PublicKey) loadKey(filepath, true);
    }

    /**
     * Loads a private key from a file.
     * 
     * @param filepath the path to the file containing the private key
     * @return the private key
     */
    public static PrivateKey loadPrivateKey(final String filepath) {
        return (PrivateKey) loadKey(filepath, false);
    }

    /**
     * Loads a key (public or private) from a file.
     * 
     * @param filepath  the path to the file containing the key
     * @param publicKey true if the key is a public key, false if it is a private key
     * @return the key
     */
    private static Key loadKey(final String filepath, final boolean publicKey) {
        final String key = removePemHeaders(fileToString(filepath));
        final byte[] decodedKey = Base64.getDecoder().decode(key);
        try {
            final KeyFactory kf = KeyFactory.getInstance("ed25519");
            if (publicKey) {
                final X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
                return kf.generatePublic(spec);
            } else {
                final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
                return kf.generatePrivate(spec);
            }
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Error loading key: " + filepath + " - " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes the given content to a file at the specified path.
     * 
     * @param content  the content to write to the file
     * @param filePath the path to the file
     */
    public static void writeToFile(final String content, final String filePath) {
        writeToFile(content.getBytes(), filePath);
    }

    /**
     * Writes the given content to a file at the specified path.
     * 
     * @param content  the content to write to the file
     * @param filePath the path to the file
     */
    public static void writeToFile(final byte[] content, final String filePath) {
        try (final OutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes PEM headers and footers from the given PEM content.
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
}
