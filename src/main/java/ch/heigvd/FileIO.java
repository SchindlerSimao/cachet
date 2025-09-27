package ch.heigvd;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class FileIO {
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

    public static PrivateKey loadPrivateKey(final String filepath) {
        final byte[] keyBytes = fileToBytes(filepath);
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        try{
            final KeyFactory kf = KeyFactory.getInstance("EC");
            return kf.generatePrivate(spec);
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException e){
            System.err.println("Error loading private key: " + filepath);
            return null;
        }
    }

    public static String writeToFile(final String content, final String filePath){
        try(final OutputStream outputStream = new FileOutputStream(filePath)){
            outputStream.write(content.getBytes());
            return filePath;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);

            throw new RuntimeException(e);
        }
    }
}
