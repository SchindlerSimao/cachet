package ch.heigvd.commands;

import picocli.CommandLine;

@CommandLine.Command(name = "sign", description = "Signe un fichier")
class Sign implements Runnable {

    @CommandLine.Parameters(index = "0", description = "Fichier d'entrée à signer")
    private String inputFile;

    @CommandLine.Parameters(index = "1", description = "Fichier de sortie pour la signature")
    private String outputSignature;

    @Override
    public void run() {
        System.out.printf("Signature de %s vers %s%n", inputFile, outputSignature);
    }
}
