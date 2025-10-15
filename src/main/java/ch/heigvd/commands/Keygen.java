package ch.heigvd.commands;

import ch.heigvd.exceptions.CachetException;
import ch.heigvd.utils.KeyUtils;
import picocli.CommandLine;

@CommandLine.Command(name = "keygen", description = "Génère une identité (clé privée) aléatoire pour la signature")
class Keygen implements Runnable {

    @CommandLine.Option(names = { "--private" }, description = "Fichier de sortie pour la clé privée")
    private String outputFile;

    @CommandLine.Option(names = { "--public" }, description = "Fichier de sortie pour la clé publique")
    private String publicKeyFile;

    @Override
    public void run() {
        try {
            if (outputFile == null || outputFile.trim().isEmpty()) {
                outputFile = "private_key.pem";
            }

            System.out.println("Génération d'une nouvelle paire de clés...");

            java.security.KeyPair keyPair = KeyUtils.generateKeyPair();

            KeyUtils.writePrivateKey(outputFile, keyPair.getPrivate());
            System.out.println("Clé privée enregistrée dans : " + outputFile);

            if (publicKeyFile != null && !publicKeyFile.trim().isEmpty()) {
                KeyUtils.writePublicKey(publicKeyFile, keyPair.getPublic());
                System.out.println("Clé publique enregistrée dans : " + publicKeyFile);
            } else {
                System.out.println("Note : Aucune clé publique n'a été enregistrée (utilisez --public pour spécifier un fichier)");
            }

            System.out.println("Génération de clés terminée avec succès");
        } catch (CachetException e) {
            System.err.println("Erreur : " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
