package ch.heigvd.commands;

import ch.heigvd.utils.FileIOUtils;
import ch.heigvd.utils.SignatureUtils;
import picocli.CommandLine;

@CommandLine.Command(name = "keygen", description = "Génère une identité (clé privée) aléatoire pour la signature")
class Keygen implements Runnable {

    @CommandLine.Option(names = { "--private" }, description = "Fichier de sortie pour la clé privée")
    private String outputFile;

    @CommandLine.Option(names = { "--public" }, description = "Output file for the public key")
    private String publicKeyFile;

    @Override
    public void run() {
        if (outputFile == null) {
            outputFile = "private_key.pem";
        }

        java.security.KeyPair keyPair = SignatureUtils.generateKeyPair();
        FileIOUtils.writePrivateKey(outputFile, keyPair.getPrivate());
        if (publicKeyFile != null) {
            FileIOUtils.writePublicKey(publicKeyFile, keyPair.getPublic());
        }
    }
}
