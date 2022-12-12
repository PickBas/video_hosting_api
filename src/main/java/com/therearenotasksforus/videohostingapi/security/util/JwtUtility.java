package com.therearenotasksforus.videohostingapi.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.therearenotasksforus.videohostingapi.models.Role;
import com.therearenotasksforus.videohostingapi.models.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtility {

    private static final Integer accessTokenExpirationTimeMinutes = 10;
    private static final Integer refreshTokenExpirationTimeMinutes = 30;
    private static final String secret = "secret";
    private static final String ISSUER = "video_hosting_api";

    public static DecodedJWT getDecodedJwt(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }

    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes());
    }

    public static String issueAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationTimeMinutes * 60 * 1000))
                .withClaim("roles", user
                        .getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .withIssuer(ISSUER)
                .sign(JwtUtility.getAlgorithm());
    }

    public static String issueAccessTokenAuth(org.springframework.security.core.userdetails.User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", user
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .withIssuer(ISSUER)
                .sign(getAlgorithm());
    }

    public static String issueRefreshTokenAuth(org.springframework.security.core.userdetails.User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationTimeMinutes * 60 * 1000))
                .withIssuer(ISSUER)
                .sign(getAlgorithm());
    }
}
