package ch.heigvd.utils;

import ch.heigvd.Constants;
import ch.heigvd.exceptions.FileOperationException;
import ch.heigvd.exceptions.MissingParameterException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for file input/output operations.
 */
public final class FileIOUtils {
    /**
     * Private constructor to prevent instantiation.
     */
    private FileIOUtils() {}

    /**
     * Reads the content of a file and returns it as a String.
     *
     * @param filePath the path to the file
     * @return the content of the file as a String
     * @throws FileOperationException if the file cannot be read
     */
    public static String fileToString(final String filePath) {
        validateFilePath(filePath);
        validateFileExists(filePath);
        validateFileReadable(filePath);

        try (final InputStream inputStream = new FileInputStream(filePath)) {
            return new String(inputStream.readAllBytes(), Constants.CHARSET);
        } catch (FileNotFoundException e) {
            throw new FileOperationException("Fichier introuvable : " + filePath, e);
        } catch (IOException e) {
            throw new FileOperationException("Erreur lors de la lecture du fichier : " + filePath, e);
        }
    }

    /**
     * Reads the content of a file and returns it as a byte array.
     *
     * @param filePath the path to the file
     * @return the content of the file as a byte array
     * @throws FileOperationException if the file cannot be read
     */
    public static byte[] fileToBytes(final String filePath) {
        validateFilePath(filePath);
        validateFileExists(filePath);
        validateFileReadable(filePath);

        try (final InputStream inputStream = new FileInputStream(filePath)) {
            byte[] content = inputStream.readAllBytes();
            if (content.length == 0) {
                throw new FileOperationException("Le fichier est vide : " + filePath);
            }
            return content;
        } catch (FileNotFoundException e) {
            throw new FileOperationException("Fichier introuvable : " + filePath, e);
        } catch (IOException e) {
            throw new FileOperationException("Erreur lors de la lecture du fichier : " + filePath, e);
        }
    }

    /**
     * Writes the given content to a file at the specified path.
     *
     * @param content  the content to write to the file
     * @param filePath the path to the file
     * @throws FileOperationException if the file cannot be written
     */
    public static void writeToFile(final String content, final String filePath) {
        if (content == null) {
            throw new FileOperationException("Le contenu à écrire ne peut pas être nul");
        }
        writeToFile(content.getBytes(Constants.CHARSET), filePath);
    }

    /**
     * Writes the given content to a file at the specified path.
     *
     * @param content  the content to write to the file
     * @param filePath the path to the file
     * @throws FileOperationException if the file cannot be written
     */
    public static void writeToFile(final byte[] content, final String filePath) {
        validateFilePath(filePath);
        validateWritableDirectory(filePath);

        if (content == null || content.length == 0) {
            throw new FileOperationException("Le contenu à écrire est vide ou nul");
        }

        try (final OutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(content);
        } catch (IOException e) {
            throw new FileOperationException("Erreur lors de l'écriture dans le fichier : " + filePath, e);
        }
    }

    /**
     * Validates that a parameter is not null or empty. If it is, prints the error message and exits.
     *
     * @param parameter    the parameter to validate
     * @param errorMessage the error message to print if the parameter is invalid
     */
    public static void validateParameters(final String parameter, final String errorMessage) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new MissingParameterException(errorMessage);
        }
    }


    /**
     * Validates that a file path is not null or empty.
     *
     * @param filePath the file path to validate
     * @throws FileOperationException if the file path is invalid
     */
    public static void validateFilePath(final String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new FileOperationException("Le chemin du fichier ne peut pas être vide ou nul");
        }
    }

    /**
     * Validates that a file exists.
     *
     * @param filePath the file path to validate
     * @throws FileOperationException if the file does not exist
     */
    private static void validateFileExists(final String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileOperationException("Le fichier n'existe pas : " + filePath);
        }
        if (Files.isDirectory(path)) {
            throw new FileOperationException("Le chemin spécifié est un répertoire, pas un fichier : " + filePath);
        }
    }

    /**
     * Validates that a file is readable.
     *
     * @param filePath the file path to validate
     * @throws FileOperationException if the file is not readable
     */
    private static void validateFileReadable(final String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.isReadable(path)) {
            throw new FileOperationException("Le fichier n'est pas accessible en lecture : " + filePath);
        }
    }

    /**
     * Validates that the directory for a file path is writable.
     *
     * @param filePath the file path to validate
     * @throws FileOperationException if the directory is not writable
     */
    public static void validateWritableDirectory(final String filePath) {
        Path path = Paths.get(filePath);
        Path parentDir = path.getParent();

        if (parentDir == null) {
            parentDir = Paths.get(".");
        }

        if (!Files.exists(parentDir)) {
            throw new FileOperationException("Le répertoire parent n'existe pas : " + parentDir);
        }

        if (!Files.isDirectory(parentDir)) {
            throw new FileOperationException("Le chemin parent n'est pas un répertoire : " + parentDir);
        }

        if (!Files.isWritable(parentDir)) {
            throw new FileOperationException("Le répertoire n'est pas accessible en écriture : " + parentDir);
        }
    }
}
