package com.therearenotasksforus.videohostingapi.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.therearenotasksforus.videohostingapi.dto.auth.AuthenticationRequestDto;
import com.therearenotasksforus.videohostingapi.dto.user.UserDto;
import com.therearenotasksforus.videohostingapi.dto.user.UserRegistrationDto;
import com.therearenotasksforus.videohostingapi.models.Role;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.security.JwtTokenService;
import com.therearenotasksforus.videohostingapi.security.util.JwtUtility;
import com.therearenotasksforus.videohostingapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenService jwtTokenService,
                                    UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @PostMapping("login")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(username, requestDto.getPassword());
            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            Map<String, String> tokens = jwtTokenService.generateTokens(auth);
            Map<String, String> response = new HashMap<>();
            response.put("access_token", tokens.get("access_token"));
            response.put("refresh_token", tokens.get("refresh_token"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("Error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    public void passwordValidation(String password) throws Exception {
        Pattern passwordPattern = Pattern.compile(
                "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = passwordPattern.matcher(password);
        if (matcher.find()) {
            return;
        }
        throw new Exception("the password must contain lowercase letters, special characters and digits!");
    }

    public void emailValidation(String email) throws Exception {
        Pattern emailPattern = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        if (matcher.find()) {
            return;
        }
        throw new Exception("invalid email!");
    }

    @CrossOrigin
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto requestDto) {
        try {
            passwordValidation(requestDto.getPassword());
            emailValidation(requestDto.getEmail());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(entry("Error", e.getMessage()));
        }
        try {
            User userToRegister = new User();
            userToRegister.setUsername(requestDto.getUsername());
            userToRegister.setEmail(requestDto.getEmail());
            userToRegister.setPassword(requestDto.getPassword());
            userService.register(userToRegister);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserDto.fromUser(userService.findById(userToRegister.getId())));
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(entry("Error", e.getMessage()));
        }
    }

    @CrossOrigin
    @PostMapping("password/update")
    public ResponseEntity<?> updatePassword(Principal principal,
                                            @RequestBody Map<String, String> passwordUpdateInfo) {
        User currentUser = userService.findByUsername(principal.getName());
        String oldPassword = passwordUpdateInfo.get("old_password");
        String updatedPassword = passwordUpdateInfo.get("updated_password");
        if (oldPassword == null || oldPassword.isEmpty() || updatedPassword == null || updatedPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            passwordValidation(passwordUpdateInfo.get("updated_password"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(entry("Error", e.getMessage()));
        }
        if (!new BCryptPasswordEncoder().matches(passwordUpdateInfo.get("old_password"),
                currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(entry("Error",
                    "Invalid old password!"));
        }
        userService.updatePassword(currentUser, passwordUpdateInfo.get("updated_password"));
        return ResponseEntity.status(HttpStatus.OK).body(currentUser);
    }

    @GetMapping("token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = JwtUtility.getDecodedJwt(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.findByUsername(username);
                String accessToken = JwtUtility.issueAccessToken(user);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        Map.of("access_token", accessToken, "refresh_token", refreshToken)
                );
            } catch (Exception e) {
                response.setStatus(403);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), Map.of("error_message", e.getMessage()));
            }
        } else {
            throw new IllegalArgumentException("Refresh token is invalid");
        }
    }

}