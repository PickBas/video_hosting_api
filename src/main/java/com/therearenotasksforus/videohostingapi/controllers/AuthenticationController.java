package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.auth.AuthenticationRequestDto;
import com.therearenotasksforus.videohostingapi.dto.user.UserDto;
import com.therearenotasksforus.videohostingapi.dto.user.UserRegistrationDto;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.security.jwt.JwtTokenProvider;
import com.therearenotasksforus.videohostingapi.service.UserService;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider,
                                    UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(username, requestDto.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            User user = userService.findByUsername(username);
            if (user == null) {
                Map<String, String> response = new HashMap<>();
                response.put("Error", "User was not found");
                return ResponseEntity.badRequest().body(response);
            }
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            Map<String, String> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
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
        if (StringUtils.isNullOrEmpty(passwordUpdateInfo.get("old_password"))
                || (StringUtils.isNullOrEmpty(passwordUpdateInfo.get("updated_password")))) {
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

}