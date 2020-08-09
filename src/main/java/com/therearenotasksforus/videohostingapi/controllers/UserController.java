package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.user.UpdateUserDto;
import com.therearenotasksforus.videohostingapi.dto.user.UserDto;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    @CrossOrigin
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAll();
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            UserDto userDtoToAdd = UserDto.fromUser(user);
            result.add(userDtoToAdd);
        }

        return result;
    }

    @GetMapping("/api/user")
    @CrossOrigin
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        try {
            UserDto userDto = UserDto.fromUser(userService.findByUsername(principal.getName()));
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/api/user/id/{id}")
    @CrossOrigin
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.ok(UserDto.fromUser(user));
    }

    @PostMapping("/api/user/update")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> updateUser(Principal principal, @RequestBody UpdateUserDto requestDto) {
        User userToUpdate;
        Map<String, String> response = new HashMap<>();

        try {
             userToUpdate = userService.findByUsername(principal.getName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            userService.updateNames(userToUpdate, requestDto);
        } catch (ValidationException e) {
            response.put("Error", "Invalid data was provided!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("Success", "The user has been updated!");
        return ResponseEntity.ok(response);
    }

}
