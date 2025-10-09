package ch.heigvd.utils;

import ch.heigvd.SignatureConstants;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Utility class for file input/output operations and key loading.
 */
public class FileIOUtils {
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
            final KeyFactory kf = KeyFactory.getInstance(SignatureConstants.SIGNATURE_ALGORITHM);
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

    public static void writePublicKey(final PublicKey publicKey) {
        writePublicKey("./public_key.pem", publicKey);
    }

    public static void writePrivateKey(final PrivateKey privateKey) {
        writePrivateKey("./private_key.pem", privateKey);
    }

    public static void writePrivateKey(final String filePath, final PrivateKey privateKey) {
        writeKey(filePath, privateKey, false);
    }

    public static void writePublicKey(final String filePath, final PublicKey publicKey) {
        writeKey(filePath, publicKey, true);
    }

    private static void writeKey(final String filePath, final Key key, final boolean publicKey){
        final String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        final String pemKey = addPemHeaders(base64Key, publicKey);

        writeToFile(pemKey, filePath);
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

    private static String addPemHeaders(final String base64Key, final boolean publicKey) {
        StringBuilder pem = new StringBuilder();
        if (publicKey) {
            pem.append("-----BEGIN PUBLIC KEY-----\n");
        } else {
            pem.append("-----BEGIN PRIVATE KEY-----\n");
        }

        // Split the base64 string into lines of 64 characters
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
