package am.aca.generactive.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@TestConfiguration
public class TestSecurityConfig {


    public String adminJWT() {
        long now = new Date().getTime();
        Date validity = new Date(now + (125 * 60 * 1000));
        return JWT.create()
                .withSubject("admin")
                .withClaim("authorities", "ADMIN")
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC512("vNlPXxzlSTn3RT5uSxPQVvCdq2GZHZ8H8Y5R4Mdq"));
    }

}