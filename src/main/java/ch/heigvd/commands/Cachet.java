package ch.heigvd.commands;

import picocli.CommandLine;

@CommandLine.Command(name = "cachet", subcommands = {
        Sign.class,
        Keygen.class,
        Verify.class
})

public class Cachet implements Runnable {
    @Override
    public void run() {
        System.out.println("Utilisez une sous-commande : sign ou verify");
    }
}
