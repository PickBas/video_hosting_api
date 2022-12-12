package com.therearenotasksforus.videohostingapi.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.therearenotasksforus.videohostingapi.security.util.JwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private static final List<String> ALLOWED_URLS
            = List.of("/api/auth/login", "/api/auth/register", "/api/auth/token/refresh");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (ALLOWED_URLS.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            String authHeader = request.getHeader(AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    DecodedJWT decodedJWT = JwtUtility.getDecodedJwt(authHeader.substring("Bearer ".length()));
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    response.setStatus(403);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(
                            response.getOutputStream(), Map.of("error_message", e.getMessage())
                    );
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
