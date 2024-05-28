package com.bunsen.ecommerce.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bunsen.ecommerce.model.LocalUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiryInSeconds}")
    private int expireInSeconds;

    private Algorithm algorithm;
    private static final String USERNAME_KEY = "USERNAME";
    private static final String EMAIL_KEY = "EMAIL";

    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateJWT(LocalUser user) {
        return JWT.create().withClaim(USERNAME_KEY, user.getUsername())
                .withExpiresAt((new Date(System.currentTimeMillis() + (1000 * expireInSeconds))))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsername(String token){
        return JWT.decode(token).getClaim(USERNAME_KEY).asString();
    }

    public String generateVerificationJWT(LocalUser user){
        return JWT.create().withClaim(EMAIL_KEY, user.getEmail())
                .withExpiresAt((new Date(System.currentTimeMillis() + (1000 * expireInSeconds))))
                .withIssuer(issuer)
                .sign(algorithm);
    }
}