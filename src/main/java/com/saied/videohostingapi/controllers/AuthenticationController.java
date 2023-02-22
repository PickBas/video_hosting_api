package com.saied.videohostingapi.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saied.videohostingapi.dto.auth.AuthenticationRequestDto;
import com.saied.videohostingapi.dto.auth.UpdatePasswordDto;
import com.saied.videohostingapi.dto.user.UserDto;
import com.saied.videohostingapi.dto.user.UserRegistrationDto;
import com.saied.videohostingapi.models.User;
import com.saied.videohostingapi.security.JwtTokenService;
import com.saied.videohostingapi.security.util.JwtUtility;
import com.saied.videohostingapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static java.util.Map.entry;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/api/auth/")
@RequiredArgsConstructor @Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @ApiResponse(
        responseCode = "200",
        description = "Logged in",
        content = @Content(mediaType = "application/json")
    )
    @PostMapping("login")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationRequestDto requestDto) {
        String username = requestDto.getUsername();
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(username, requestDto.getPassword());
            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            Map<String, String> tokens = jwtTokenService.generateTokens(auth);
            log.info("User with username {} logged in. HttpStatus: {}", username, HttpStatus.OK);
            return ResponseEntity.ok(Map.of(
                    "access_token", tokens.get("access_token"),
                    "refresh_token", tokens.get("refresh_token")));
        } catch (Exception e) {
            log.warn("User with username {} could not log in. HttpStatus: {}. Error: {}", username, 400, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error_message", e.getMessage()));
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
        throw new Exception("Password must contain lowercase letters, special characters and digits.");
    }

    public void emailValidation(String email) throws Exception {
        Pattern emailPattern = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        if (matcher.find()) {
            return;
        }
        throw new Exception("invalid email.");
    }

    @ApiResponse(
        responseCode = "201",
        description = "Registered",
        content = @Content(mediaType = "application/json")
    )
    @CrossOrigin
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto requestDto) {
        try {
            passwordValidation(requestDto.getPassword());
            emailValidation(requestDto.getEmail());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(entry("error_message", e.getMessage()));
        }
        try {
            User userToRegister = new User();
            userToRegister.setUsername(requestDto.getUsername());
            userToRegister.setEmail(requestDto.getEmail());
            userToRegister.setPassword(requestDto.getPassword());
            userService.register(userToRegister);
            log.info("User with username {} registered. HttpStatus: {}",
                    userToRegister.getUsername(), HttpStatus.CREATED);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserDto.fromUser(userService.findById(userToRegister.getId())));
        } catch (IllegalStateException e) {
            log.warn("Could not register user with username {}. HttpStatus: {}. Error: {}",
                    requestDto.getUsername(), HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(entry("error_message", e.getMessage()));
        }
    }

    @ApiResponse(
        responseCode = "200",
        description = "Password updated",
        content = @Content(mediaType = "application/json")
    )
    @CrossOrigin
    @PostMapping("password/update")
    public ResponseEntity<?> updatePassword(Principal principal,
                                            @RequestBody UpdatePasswordDto passwordUpdateInfo) {
        User currentUser = userService.findByUsername(principal.getName());
        String oldPassword = passwordUpdateInfo.getOldPassword();
        String updatedPassword = passwordUpdateInfo.getNewPassword();
        if (oldPassword == null || oldPassword.isEmpty() || updatedPassword == null || updatedPassword.isEmpty()) {
            log.warn("Provided passwords by user {} are not valid. HttpStatus: {}",
                    currentUser.getUsername(), HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            passwordValidation(passwordUpdateInfo.getNewPassword());
        } catch (Exception e) {
            log.warn("Provided updated password by user {} are not valid. HttpStatus: {}. Error: {}",
                    currentUser.getUsername(), HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(entry("error_message", e.getMessage()));
        }
        if (!new BCryptPasswordEncoder()
                .matches(passwordUpdateInfo.getOldPassword(), currentUser.getPassword())) {
            log.warn("User {} Entered incorrect old password. HttpStatus: {}",
                    currentUser.getUsername(), HttpStatus.BAD_REQUEST);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(entry("error_message","Invalid old password."));
        }
        userService.updatePassword(currentUser, passwordUpdateInfo.getNewPassword());
        log.info("User {} updated password. HttpStatus: {}", currentUser.getUsername(), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(currentUser);
    }

    @ApiResponse(
        responseCode = "200",
        description = "Tokens refreshed",
        content = @Content(mediaType = "application/json")
    )
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
                log.info("User {} refreshed token. HttpStatus: {}", user.getUsername(), HttpStatus.OK);
                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        Map.of("access_token", accessToken, "refresh_token", refreshToken)
                );
            } catch (Exception e) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                log.warn("Could not refresh access token. HttpStatus: {}. Error: {}",
                        HttpStatus.FORBIDDEN, e.getMessage());
                new ObjectMapper().writeValue(
                        response.getOutputStream(), Map.of("error_message", e.getMessage())
                );
            }
        } else {
            log.warn("Invalid refresh token. HttpStatus: {}", HttpStatus.BAD_REQUEST);
            throw new IllegalArgumentException("Refresh token is invalid");
        }
    }

}