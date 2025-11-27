package ch.heigvd.cachet.commands;

import picocli.CommandLine;

@CommandLine.Command(
    name = "cachet",
    subcommands = {
        Sign.class,
        Keygen.class,
        Verify.class,
        CommandLine.HelpCommand.class
    },
    description = "Outil CLI pour signer et vérifier des documents numériques avec Ed25519",
    mixinStandardHelpOptions = true,
    version = "Cachet 1.0"
)
public class Cachet implements Runnable {
    @Override
    public void run() {
        System.out.println("""
                Cachet - Signature de documents numériques
                ==========================================
                Utilisez une des sous-commandes suivantes :
                  keygen  - Génère une nouvelle paire de clés
                  sign    - Signe un fichier
                  verify  - Vérifie la signature d'un fichier
                """);
    }
}
