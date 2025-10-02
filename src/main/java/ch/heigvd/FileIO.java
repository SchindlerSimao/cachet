package ch.heigvd;

import java.io.*;

public class FileIO {
    public static String readFileString(final File filePath) {
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

    public static byte[] readFileRaw(final File filePath) {
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

    public static void writeFileRaw(final byte[] content, final File filePath) {
        try (final OutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            throw new RuntimeException(e);
        }
    }

    public static void writeFileString(final String content, final File filePath) {
        try (final OutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            throw new RuntimeException(e);
        }
    }
}
