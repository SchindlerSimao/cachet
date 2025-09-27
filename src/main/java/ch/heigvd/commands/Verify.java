package ch.heigvd.commands;

import picocli.CommandLine;

@CommandLine.Command(name = "verify", description = "Vérifie la signature d'un fichier")
class Verify implements Runnable {

    @CommandLine.Parameters(index = "0", description = "Fichier d'entrée à vérifier")
    private String inputFile;

    @CommandLine.Parameters(index = "1", description = "Fichier contenant la signature")
    private String signatureFile;

    @CommandLine.Parameters(index = "2", description = "Clé publique pour la vérification")
    private String publicKey;

    @Override
    public void run() {
        System.out.printf("Vérification de %s avec signature %s et clé %s%n",
                inputFile, signatureFile, publicKey);
    }
}
