package ch.heigvd.commands;

import ch.heigvd.exceptions.CachetException;
import ch.heigvd.utils.FileIOUtils;
import ch.heigvd.utils.SignatureUtils;
import picocli.CommandLine;

import java.security.PrivateKey;
import java.util.Base64;

@CommandLine.Command(name = "sign", description = "Signe un fichier")
class Sign implements Runnable {

    @CommandLine.Parameters(index = "0", description = "Chemin vers le fichier d'entrée à signer")
    private String inputFilePath;

    @CommandLine.Parameters(index = "1", description = "Chemin vers le fichier de sortie pour la signature")
    private String outputSignaturePath;

    @CommandLine.Parameters(index = "2", description = "Chemin vers la clé privée")
    private String privateKeyPath;

    @Override
    public void run() {
        try {
            // Validate input parameters
            if (inputFilePath == null || inputFilePath.trim().isEmpty()) {
                System.err.println("Erreur : Le chemin du fichier d'entrée est requis");
                System.exit(1);
                return;
            }
            if (outputSignaturePath == null || outputSignaturePath.trim().isEmpty()) {
                System.err.println("Erreur : Le chemin du fichier de sortie est requis");
                System.exit(1);
                return;
            }
            if (privateKeyPath == null || privateKeyPath.trim().isEmpty()) {
                System.err.println("Erreur : Le chemin de la clé privée est requis");
                System.exit(1);
                return;
            }

            System.out.printf("Signature de %s vers %s%n", inputFilePath, outputSignaturePath);

            System.out.println("Lecture du fichier...");
            final byte[] dataToSign = FileIOUtils.fileToBytes(inputFilePath);
            System.out.printf("Fichier lu (%d octets)%n", dataToSign.length);

            System.out.println("Chargement de la clé privée...");
            final PrivateKey privateKey = FileIOUtils.loadPrivateKey(privateKeyPath);
            System.out.println("Clé privée chargée");

            System.out.println("Signature en cours...");
            final byte[] signature = SignatureUtils.sign(dataToSign, privateKey);
            System.out.println("Signature générée");

            final byte[] encodedSignature = Base64.getEncoder().encode(signature);

            System.out.println("Signature (Base64) : " + new String(encodedSignature));

            System.out.println("Écriture de la signature...");
            FileIOUtils.writeToFile(encodedSignature, outputSignaturePath);
            System.out.println("Signature enregistrée dans : " + outputSignaturePath);

            System.out.println("✓ Signature terminée avec succès");
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
