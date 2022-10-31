package com.example.mulinge.jwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;

public class JWTConfig {
    private static final Algorithm algorithm = Algorithm.HMAC256("my-secret");

    public static String createAccessToken(String username, List<String> roles, Date expiresAt) {

        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("roles", roles)
                    .withIssuer("muling3")
                    .withExpiresAt(expiresAt)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new JWTCreationException(exception.getMessage(), exception.getCause());
        }

    }

    public static String createRefreshToken(String username, List<String> roles, Date expiresAt) {

        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("roles", roles)
                    .withIssuer("muling3")
                    .withExpiresAt(expiresAt)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new JWTCreationException(exception.getMessage(), exception.getCause());
        }

    }

    public static DecodedJWT verifyAccessToken(String accessToken) {

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("muling3")
                    .build();
           return verifier.verify(accessToken);
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException(exception.getMessage(), exception.getCause());
        }
    }

}
