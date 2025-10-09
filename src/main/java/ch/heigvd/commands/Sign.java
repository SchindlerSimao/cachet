package ch.heigvd.commands;

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
        final byte[] dataToSign = FileIOUtils.fileToBytes(inputFilePath);
        final PrivateKey privateKey = FileIOUtils.loadPrivateKey(privateKeyPath);

        System.out.printf("Signature de %s vers %s%n", inputFilePath, outputSignaturePath);

        final byte[] signature = SignatureUtils.sign(dataToSign, privateKey);

        final byte[] encodedSignature = Base64.getEncoder().encode(signature);

        System.out.println("Signature :" + new String(encodedSignature));
        FileIOUtils.writeToFile(encodedSignature, outputSignaturePath);

        System.out.println("Terminé.");
    }
}
