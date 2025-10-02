package ch.heigvd.commands;

import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "sign", description = "Signe un fichier")
class Sign implements Runnable {

    @CommandLine.Parameters(index = "0", description = "Fichier d'entrée à signer")
    private File inputFile;

    @CommandLine.Option(names = { "-i",
            "--identity" }, description = "Fichier d'identité contenant la clé privée", required = true)
    private File identityFile;

    @CommandLine.Option(names = { "-o", "--out" }, description = "Fichier de sortie pour la signature")
    private File outputSignature;

    @Override
    public void run() {
    }
}
