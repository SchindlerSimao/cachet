package ch.heigvd.commands;

import ch.heigvd.FileIO;
import ch.heigvd.SignatureOperations;
import picocli.CommandLine;

import java.security.PrivateKey;

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
        final byte[] dataToSign = FileIO.fileToBytes(inputFilePath);
        final PrivateKey privateKey = FileIO.loadPrivateKey(privateKeyPath);

        System.out.printf("Signature de %s vers %s%n", inputFilePath, outputSignaturePath);

        FileIO.writeToFile(SignatureOperations.sign(dataToSign, privateKey), outputSignaturePath);

        System.out.println("Terminé.");
    }
}
