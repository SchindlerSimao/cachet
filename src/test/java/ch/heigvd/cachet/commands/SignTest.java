package ch.heigvd.cachet.commands;

import ch.heigvd.cachet.Constants;
import ch.heigvd.cachet.utils.SignatureUtils;
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
                .getInstance(Constants.SIGNATURE_ALGORITHM)
                .generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        message = "Hello, world!".getBytes();
    }

    @Test
    public void testSignatureIsValid() throws Exception {
        final byte[] signature = SignatureUtils.sign(message, privateKey);

        final Signature verifier = Signature.getInstance(Constants.SIGNATURE_ALGORITHM);
        verifier.initVerify(publicKey);
        verifier.update(message);
        assertTrue(verifier.verify(signature));
    }

    @Test
    public void testSignatureIsNotNullOrEmpty() {
        final byte[] signature = SignatureUtils.sign(message, privateKey);

        assertNotNull(signature);
        assertTrue(signature.length > 0);
    }
}
