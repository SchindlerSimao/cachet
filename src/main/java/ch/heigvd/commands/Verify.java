package ch.heigvd.commands;

import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "verify", description = "Vérifie la signature d'un fichier")
class Verify implements Runnable {

    @CommandLine.Parameters(index = "0", description = "Fichier d'entrée à vérifier")
    private File inputFile;

    @CommandLine.Parameters(index = "1", description = "Fichier contenant la signature")
    private File signatureFile;

    @CommandLine.Parameters(index = "2", description = "Clé publique pour la vérification")
    private File publicKey;

    @Override
    public void run() {
        System.out.printf("Vérification de %s avec signature %s et clé %s%n",
                inputFile, signatureFile, publicKey);
    }
}
