package com.therearenotasksforus.videohostingapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    public Map<String, String> generateTokens(Authentication auth) {
        User user = (User)auth.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return Map.of("access_token", generateAccessToken(user, algorithm),
                "refresh_token", generateRefreshToken(user, algorithm));
    }

    private String generateAccessToken(User user, Algorithm algorithm) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", user
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .withIssuer("video_hosting_api")
                .sign(algorithm);
    }

    private String generateRefreshToken(User user, Algorithm algorithm) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer("video_hosting_api")
                .sign(algorithm);
    }

}
