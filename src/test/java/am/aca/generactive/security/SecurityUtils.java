package am.aca.generactive.security;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SecurityUtils {

    private final TokenProvider tokenProvider;

    public SecurityUtils(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String getAdminJWT() {
        long now = new Date().getTime();
        Date validity = new Date(now + (125 * 60 * 1000));
        return tokenProvider.createToken("admin", "ADMIN", validity);
    }

    public String getUserJWT() {
        long now = new Date().getTime();
        Date validity = new Date(now + (125 * 60 * 1000));
        return tokenProvider.createToken("user", "USER", validity);
    }
}
