package ch.heigvd.cachet;

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

    /**
     * The character set used for encoding and decoding strings.
     */
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * Exit code indicating a general error occurred.
     */
    public static final int ERROR_EXIT_CODE = 1;

    /**
     * Exit code indicating the signature is invalid.
     */
    public static final int INVALID_SIGNATURE_EXIT_CODE = 2;
}
