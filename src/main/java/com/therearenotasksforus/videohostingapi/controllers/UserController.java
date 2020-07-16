package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.user.UpdateUserDto;
import com.therearenotasksforus.videohostingapi.dto.user.UserDto;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
    public UserDto getCurrentUser(Principal principal) {
        try {
            return UserDto.fromUser(userService.findByUsername(principal.getName()));
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/api/user/id/{id}")
    @CrossOrigin
    public UserDto getUserById(@PathVariable(name = "id") Long id) {
        return UserDto.fromUser(userService.findById(id));
    }

    @PostMapping("/api/user/update")
    @CrossOrigin
    public String updateUser(Principal principal, @RequestBody UpdateUserDto requestDto) {
        User userToUpdate;

        try {
             userToUpdate = userService.findByUsername(principal.getName());
        } catch (Exception e) {
            return "Failure: cannot find the user!";
        }

        try {
            userService.updateNames(userToUpdate, requestDto);
        } catch (ValidationException e) {
            return "Failure: invalid data was provided!";
        }

        return "Success: the user has been updated!";
    }

}
