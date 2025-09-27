package ch.heigvd;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class FileIO {
    /**
     * Reads the content of a file and returns it as a String.
     * @param filePath the path to the file
     * @return the content of the file as a String
     */
    public static String fileToString(final String filePath){
        try(final InputStream inputStream = new FileInputStream(filePath)){
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
     * @param filePath the path to the file
     * @return the content of the file as a byte array
     */
    public static byte[] fileToBytes(final String filePath){
        try(final InputStream inputStream = new FileInputStream(filePath)){
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
     * @param filepath the path to the file containing the public key
     * @return the public key
     */
    public static PublicKey loadPublicKey(final String filepath) {
        return (PublicKey) loadKey(filepath, true);
    }

    /**
     * Loads a private key from a file.
     * @param filepath the path to the file containing the private key
     * @return the private key
     */
    public static PrivateKey loadPrivateKey(final String filepath) {
        return (PrivateKey) loadKey(filepath, false);
    }

    /**
     * Loads a key (public or private) from a file.
     * @param filepath the path to the file containing the key
     * @param publicKey true if the key is a public key, false if it is a private key
     * @return the key
     */
    private static Key loadKey(final String filepath, final boolean publicKey) {
        final byte[] keyBytes = fileToBytes(filepath);
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        try{
            final KeyFactory kf = KeyFactory.getInstance("EC");
            if(publicKey)
                return kf.generatePublic(spec);
            return kf.generatePrivate(spec);
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException e){
            System.err.println("Error loading private key: " + filepath);
            return null;
        }
    }

    /**
     * Writes the given content to a file at the specified path.
     * @param content the content to write to the file
     * @param filePath the path to the file
     */
    public static void writeToFile(final String content, final String filePath){
        try(final OutputStream outputStream = new FileOutputStream(filePath)){
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);

            throw new RuntimeException(e);
        }
    }
}
