package pl.ark.chr.buginator.util;

import pl.ark.chr.buginator.domain.auth.User;

import java.util.Base64;

/**
 * Created by Arek on 2017-05-04.
 */
public class TokenGenerator {

    private static final String TOKEN_SPLITTER = ":";

    public static String generateToken(User user) {
        String rawToken = user.getEmail() + TOKEN_SPLITTER + user.getCompany().getId();
        return encode(rawToken);
    }

    private static String encode(String rawToken) {
        return Base64.getEncoder().encodeToString(
                Base64.getEncoder().encode(rawToken.getBytes()));
    }

    public static String decode(String encodedToken) {
        byte[] encodedBytes = Base64.getDecoder().decode(
                Base64.getDecoder().decode(encodedToken));
        return new String(encodedBytes);
    }
}
