package am.aca.generactive.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {

    public String createToken(String subject, String authorities, Date expiresAt) {
        return JWT.create()
                .withSubject(subject)
                .withClaim("authorities", authorities)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512("vNlPXxzlSTn3RT5uSxPQVvCdq2GZHZ8H8Y5R4Mdq"));
    }
}
