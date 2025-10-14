package ch.heigvd.commands;

import ch.heigvd.exceptions.CachetException;
import ch.heigvd.utils.FileIOUtils;
import ch.heigvd.utils.SignatureUtils;
import picocli.CommandLine;

import java.security.PublicKey;

@CommandLine.Command(name = "verify", description = "Vérifie la signature d'un fichier")
class Verify implements Runnable {

    @CommandLine.Parameters(index = "0", description = "Chemin vers le fichier d'entrée à vérifier")
    private String inputFile;

    @CommandLine.Parameters(index = "1", description = "Chemin vers le fichier contenant la signature")
    private String signatureFile;

    @CommandLine.Parameters(index = "2", description = "Chemin vers la clé publique pour la vérification")
    private String publicKeyPath;

    @Override
    public void run() {
        try {
            // Validate input parameters
            FileIOUtils.validateParameters(inputFile, "Erreur : Le chemin du fichier d'entrée est requis");
            FileIOUtils.validateParameters(signatureFile, "Erreur : Le chemin du fichier de signature est requis");
            FileIOUtils.validateParameters(publicKeyPath, "Erreur : Le chemin de la clé publique est requis");

            System.out.printf("Vérification de %s avec signature %s et clé %s%n",
                    inputFile, signatureFile, publicKeyPath);

            System.out.println("Lecture du fichier...");
            final byte[] dataToVerify = FileIOUtils.fileToBytes(inputFile);
            System.out.printf("Fichier lu (%d octets)%n", dataToVerify.length);

            System.out.println("Chargement de la clé publique...");
            final PublicKey publicKey = FileIOUtils.loadPublicKey(publicKeyPath);
            System.out.println("Clé publique chargée");

            System.out.println("Lecture de la signature...");
            final byte[] signatureEncoded = FileIOUtils.fileToBytes(signatureFile);

            final byte[] signature;
            try {
                signature = java.util.Base64.getDecoder().decode(signatureEncoded);
                System.out.printf("Signature lue (%d octets après décodage Base64)%n", signature.length);
            } catch (IllegalArgumentException e) {
                throw new CachetException("La signature n'est pas au format Base64 valide", e);
            }

            System.out.println("Vérification en cours...");
            final boolean valid = SignatureUtils.verify(dataToVerify, signature, publicKey);

            System.out.println();
            if (valid) {
                System.out.println("La signature est valide");
                System.exit(0);
            } else {
                System.out.println("La signature est invalide");
                System.exit(1);
            }
        } catch (CachetException e) {
            System.err.println("Erreur : " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
