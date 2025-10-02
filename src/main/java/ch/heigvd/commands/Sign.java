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
    public void run() { // TODO: ? add an option to generate a key pair if none is provided ?
        final byte[] dataToSign = FileIO.fileToBytes(inputFilePath);
        final PrivateKey privateKey = FileIO.loadPrivateKey(privateKeyPath);

        System.out.printf("Signature de %s vers %s%n", inputFilePath, outputSignaturePath);

        final byte[] signature = SignatureOperations.sign(dataToSign, privateKey);
        FileIO.writeToFile(signature, outputSignaturePath);

        System.out.println("Terminé.");
    }
}
