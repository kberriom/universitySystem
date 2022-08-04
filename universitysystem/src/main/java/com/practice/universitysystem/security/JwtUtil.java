package com.practice.universitysystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("jwt_secret")
    private String secret;

    public String generateToken(String email) {
        return JWT.create()
                .withSubject("USER DETAILS")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("UNIVERSITY_SYSTEM")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateAndRetrieve(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("USER DETAILS")
                .withIssuer("UNIVERSITY_SYSTEM")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }

}
