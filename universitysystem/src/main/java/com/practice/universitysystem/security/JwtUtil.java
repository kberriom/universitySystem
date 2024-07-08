package com.practice.universitysystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("jwt_secret")
    private String secret;

    private static final long JWT_VALIDITY = 10L * 60L * 1000L;

    public String generateToken(String email, String role) {
        final long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject("USER DETAILS")
                .withClaim("email", email)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withIssuer("UNIVERSITY_SYSTEM")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(now + JWT_VALIDITY))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateAndRetrieve(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("USER DETAILS")
                .withIssuer("UNIVERSITY_SYSTEM")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}
