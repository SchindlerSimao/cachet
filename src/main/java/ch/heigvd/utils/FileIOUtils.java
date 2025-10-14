package ch.heigvd.utils;

import ch.heigvd.Constants;
import ch.heigvd.exceptions.FileOperationException;
import ch.heigvd.exceptions.KeyOperationException;
import ch.heigvd.exceptions.MissingParameterException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Utility class for file input/output operations and key loading.
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
     * Loads a key (public or private) from a file.
     * 
     * @param filepath  the path to the file containing the key
     * @param publicKey true if the key is a public key, false if it is a private key
     * @return the key
     * @throws KeyOperationException if the key cannot be loaded
     */
    private static Key loadKey(final String filepath, final boolean publicKey) {
        final String keyContent = fileToString(filepath);

        // Validate PEM format
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
     * Writes a private key to a file in PEM format.
     * @param filePath the path to the file where the private key will be written
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
     * @param filePath the path to the file where the public key will be written
     * @param publicKey the public key to write
     */
    public static void writePublicKey(final String filePath, final PublicKey publicKey) {
        if (publicKey == null) {
            throw new KeyOperationException("La clé publique ne peut pas être nulle");
        }
        writeKey(filePath, publicKey, true);
    }

    /**
     * Writes a key (public or private) to a file in PEM format.
     * @param filePath the path to the file where the key will be written
     * @param key the key to write
     * @param publicKey true if the key is a public key, false if it is a private key
     */
    private static void writeKey(final String filePath, final Key key, final boolean publicKey){
        validateFilePath(filePath);
        validateWritableDirectory(filePath);

        final String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        final String pemKey = addPemHeaders(base64Key, publicKey);

        writeToFile(pemKey, filePath);
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
     * @param parameter the parameter to validate
     * @param errorMessage the error message to print if the parameter is invalid
     */
    public static void validateParameters(final String parameter, final String errorMessage) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new MissingParameterException(errorMessage);
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

    /**
     * Adds PEM headers and footers to a base64-encoded key string.
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

    /**
     * Validates that a file path is not null or empty.
     * @param filePath the file path to validate
     * @throws FileOperationException if the file path is invalid
     */
    private static void validateFilePath(final String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new FileOperationException("Le chemin du fichier ne peut pas être vide ou nul");
        }
    }

    /**
     * Validates that a file exists.
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
     * @param filePath the file path to validate
     * @throws FileOperationException if the directory is not writable
     */
    private static void validateWritableDirectory(final String filePath) {
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
