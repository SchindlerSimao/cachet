package ch.heigvd.commands;

import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.util.encoders.Base64;

import ch.heigvd.FileIO;

@CommandLine.Command(name = "keygen", description = "Génère une identité (clé privée) aléatoire pour la signature")
class Keygen implements Runnable {

    @CommandLine.Option(names = { "-o", "--out" }, description = "Fichier de sortie pour la clé privée")
    private File outputFile;

    private String bytesToHex(byte[] input) {
        StringBuilder builder = new StringBuilder();
        for (byte b : input) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    private static byte[] PEM_PRIVATE_HEADER = "-----BEGIN PRIVATE KEY-----\n".getBytes();
    private static byte[] PEM_PRIVATE_FOOTER = "\n-----END PRIVATE KEY-----".getBytes();

    private static byte[] PEM_PUBLIC_HEADER = "-----BEGIN PUBLIC KEY-----\n".getBytes();
    private static byte[] PEM_PUBLIC_FOOTER = "\n-----END PUBLIC KEY-----".getBytes();

    private byte[] pemEncodePrivate(byte[] key) {
        return concat(PEM_PRIVATE_HEADER, Base64.encode(key), PEM_PRIVATE_FOOTER);
    }

    private byte[] pemEncodePublic(byte[] key) {
        return concat(PEM_PUBLIC_HEADER, Base64.encode(key), PEM_PUBLIC_FOOTER);
    }

    private static byte[] concat(byte[]... arrays) {
        var out = new ByteArrayOutputStream();

        for (byte[] a : arrays) {
            try {
                out.write(a);
            } catch (IOException e) {
                System.out.println("Failed to concatenate PEM file: " + e);
            }
        }
        return out.toByteArray();
    }

    @Override
    public void run() {
        var gen = new Ed25519KeyPairGenerator();
        gen.init(new KeyGenerationParameters(new SecureRandom(), 2 ^ 256));
        var keypair = gen.generateKeyPair();

        var sk = keypair.getPrivate();
        var pk = keypair.getPublic();

        byte[] skBytes = ((Ed25519PrivateKeyParameters) sk).getEncoded();
        byte[] pkBytes = ((Ed25519PublicKeyParameters) pk).getEncoded();

        var skPem = pemEncodePrivate(skBytes);
        var pkPem = pemEncodePublic(pkBytes);

        if (outputFile == null) {
            System.out.println(new String(skPem));
            System.out.println(new String(pkPem));
        } else {
            FileIO.writeFileRaw(skPem, outputFile);
            var outputPublic = outputFile.getPath() + ".pub";
            FileIO.writeFileRaw(pkPem, new File(outputPublic));
            System.out.println("File has been written to " + outputFile);
        }

    }
}
