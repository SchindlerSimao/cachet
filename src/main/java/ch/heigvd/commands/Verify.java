package ch.heigvd.commands;

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
    private String publicKey;

    @Override
    public void run() {
        final byte[] dataToVerify = FileIOUtils.fileToBytes(inputFile);
        final PublicKey publicKey = FileIOUtils.loadPublicKey(this.publicKey);
        final byte[] signature = FileIOUtils.fileToBytes(signatureFile);

        System.out.printf("Vérification de %s avec signature %s et clé %s%n",
                inputFile, signatureFile, publicKey);

        final boolean valid = SignatureUtils.verify(dataToVerify, signature, publicKey);

        System.out.printf("La signature est %s%n", valid ? "valide" : "invalide");
    }
}
