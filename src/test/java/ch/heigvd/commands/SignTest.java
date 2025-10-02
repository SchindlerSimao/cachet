package ch.heigvd.commands;

import ch.heigvd.SignatureOperations;
import org.junit.Before;
import org.junit.Test;

import java.security.*;

import static junit.framework.TestCase.*;


public class SignTest {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private byte[] message;

    @Before
    public void setUp() throws Exception {
        final KeyPair keyPair = KeyPairGenerator
                .getInstance("ed25519")
                .generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        message = "Hello, world!".getBytes();
    }

    @Test
    public void testSignatureIsValid() throws Exception {
        final byte[] signature = SignatureOperations.sign(message, privateKey);

        final Signature verifier = Signature.getInstance("ed25519");
        verifier.initVerify(publicKey);
        verifier.update(message);
        assertTrue(verifier.verify(signature));
    }

    @Test
    public void testSignatureIsNotNullOrEmpty() {
        final byte[] signature = SignatureOperations.sign(message, privateKey);

        assertNotNull(signature);
        assertTrue(signature.length > 0);
    }
}
