package ch.heigvd;

import java.io.*;

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
