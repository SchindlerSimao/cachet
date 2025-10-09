package ch.heigvd.commands;

import ch.heigvd.utils.FileIOUtils;
import ch.heigvd.utils.SignatureUtils;
import picocli.CommandLine;
import java.security.PrivateKey;

@CommandLine.Command(name = "keygen", description = "Génère une identité (clé privée) aléatoire pour la signature")
class Keygen implements Runnable {

    @CommandLine.Option(names = { "-o", "--out" }, description = "Fichier de sortie pour la clé privée")
    private String outputFile;

    @Override
    public void run() {
        if (outputFile == null) {
            outputFile = "private_key.pem";
        }

        PrivateKey privateKey = SignatureUtils.generatePrivateKey();

        FileIOUtils.writePrivateKey(privateKey);
    }
}
