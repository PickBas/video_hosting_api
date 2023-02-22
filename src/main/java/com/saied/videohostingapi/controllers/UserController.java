package com.saied.videohostingapi.controllers;

import com.saied.videohostingapi.dto.user.UpdateUserDto;
import com.saied.videohostingapi.dto.user.UserDto;
import com.saied.videohostingapi.models.User;
import com.saied.videohostingapi.service.UserService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequiredArgsConstructor @Slf4j
public class UserController {

    private final UserService userService;

    @Deprecated
    @ApiResponse(
        responseCode = "200",
        description = "Get all users",
        content = @Content(mediaType = "application/json")
    )
    @GetMapping("/api/users")
    @CrossOrigin
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAll();
        List<UserDto> result = new ArrayList<>();
        for (User user : users) {
            UserDto userDtoToAdd = UserDto.fromUser(user);
            result.add(userDtoToAdd);
        }
        log.info("Loaded all users. HttpStatus: {}", HttpStatus.OK);
        return result;
    }

    @Operation(summary = "Retrieve current user")
    @ApiResponse(
        responseCode = "200",
        description = "Retrieved current user",
        content = @Content(mediaType = "application/json")
    )
    @GetMapping("/api/user")
    @CrossOrigin
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        try {
            UserDto userDto = UserDto.fromUser(userService.findByUsername(principal.getName()));
            log.info("Loaded current user {}. HttpStatus: {}", principal.getName(), HttpStatus.OK);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiResponse(
        responseCode = "200",
        description = "Retrieved user by id",
        content = @Content(mediaType = "application/json")
    )
    @GetMapping("/api/user/id/{id}")
    @CrossOrigin
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Loaded user {}. HttpStatus: {}", user.getUsername(), HttpStatus.OK);
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    @ApiResponse(
        responseCode = "200",
        description = "Updated user information",
        content = @Content(mediaType = "application/json")
    )
    @PostMapping("/api/user/update")
    @CrossOrigin
    public ResponseEntity<?> updateUser(Principal principal, @RequestBody UpdateUserDto requestDto) {
        User userToUpdate;
        try {
             userToUpdate = userService.findByUsername(principal.getName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            userService.updateNames(userToUpdate, requestDto);
        } catch (ValidationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error_message", "Invalid data was provided!"));
        }
        log.info("Updated user {}. HttpStatus: {}", principal.getName(), HttpStatus.OK);
        return ResponseEntity.ok(UserDto.fromUser(userService.findById(userToUpdate.getId())));
    }

}
