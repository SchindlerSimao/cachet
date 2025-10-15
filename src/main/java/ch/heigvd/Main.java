package ch.heigvd;

import ch.heigvd.commands.Cachet;
import ch.heigvd.exceptions.CachetException;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = 0;
        try {
            exitCode = new CommandLine(new Cachet()).execute(args);
        } catch (CachetException e) {
            System.err.println("Erreur : " + e.getMessage());
            exitCode = 1;
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
            exitCode = 1;
        }
        System.exit(exitCode);
    }
}
