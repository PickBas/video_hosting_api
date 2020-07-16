package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.auth.AuthenticationRequestDto;
import com.therearenotasksforus.videohostingapi.dto.auth.LogoutRequestDto;
import com.therearenotasksforus.videohostingapi.dto.user.UserRegistrationDto;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.security.jwt.JwtTokenProvider;
import com.therearenotasksforus.videohostingapi.security.jwt.services.InvalidJwtsService;
import com.therearenotasksforus.videohostingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    @CrossOrigin
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, requestDto.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found!");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<String, String> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("register")
    public String register(@RequestBody UserRegistrationDto requestDto) {
        try {
            User userToRegister = new User();
            userToRegister.setUsername(requestDto.getUsername());
            userToRegister.setEmail(requestDto.getEmail());
            userToRegister.setPassword(requestDto.getPassword());

            userService.register(userToRegister);
            return "Success: User with username " + userToRegister.getUsername() + " has been registered!";
        } catch (Exception e) {
            return "Failure: " + e.getMessage();
        }
    }

}