package ch.heigvd;

import java.io.*;

public class FileIO {
    public static String fileToString(final String filePath){
        try(InputStream inputStream = new FileInputStream(filePath)){
            return new String(inputStream.readAllBytes());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);

            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error opening file: " + filePath);

            throw new RuntimeException(e);
        }
    }
}
