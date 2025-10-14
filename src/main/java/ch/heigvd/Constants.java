package ch.heigvd;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This class contains constants related to signature operations.
 */
public final class Constants {
    /**
     * The signature algorithm to be used for signing and verifying data.
     */
    public static final String SIGNATURE_ALGORITHM = "Ed25519";

    public static final Charset CHARSET = StandardCharsets.UTF_8;
}
