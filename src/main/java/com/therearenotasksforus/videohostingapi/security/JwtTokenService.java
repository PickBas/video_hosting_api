package com.therearenotasksforus.videohostingapi.security;

import com.therearenotasksforus.videohostingapi.security.util.JwtUtility;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtTokenService {

    public Map<String, String> generateTokens(Authentication auth) {
        User user = (User)auth.getPrincipal();
        return Map.of("access_token", generateAccessToken(user),
                "refresh_token", generateRefreshToken(user));
    }

    private String generateAccessToken(User user) {
        return JwtUtility.issueAccessTokenAuth(user);
    }

    private String generateRefreshToken(User user) {
        return JwtUtility.issueRefreshTokenAuth(user);
    }

}
