package ch.heigvd.cachet;

import ch.heigvd.cachet.commands.Cachet;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Cachet()).execute(args);
        System.exit(exitCode);
    }
}
