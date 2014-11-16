package cc.sortland.poll;

import com.fasterxml.jackson.annotation.JsonValue;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author gunnaringe
 */
public final class Id {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final int SIZE = 21;

    private final String value;

    private Id(final String value) {
        validate(value);
        this.value = value;
    }

    public static Id valueOf(final String value) {
        if (value == null) { return null; }
        return new Id(value);
    }


    public static Id generate() {
        final SecureRandom secureRandom = Holder.numberGenerator;
        byte[] bytes = new byte[SIZE];
        secureRandom.nextBytes(bytes);
        final String value = new String(Holder.encoder.encode(bytes), UTF_8);
        return new Id(value);
    }

    private static void validate(String value) {
        final byte[] bytes = Holder.decoder.decode(value);
        if (bytes.length != SIZE) {
            throw new IllegalArgumentException("Invalid value");
        }
    }

    @JsonValue
    public String getStringValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Id{" + value + "}";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) { return true; }
        if (other == null || getClass() != other.getClass()) { return false; }
        final Id id = (Id) other;
        return id.value.equals(((Id) other).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    private static class Holder {
        private static final SecureRandom numberGenerator = new SecureRandom();
        private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        private static final Base64.Decoder decoder = Base64.getUrlDecoder();
    }
}
