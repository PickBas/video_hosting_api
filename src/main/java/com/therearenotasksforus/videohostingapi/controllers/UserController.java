package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.UserDto;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.service.UserService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get/users/")
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAll();
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            UserDto userDtoToAdd = UserDto.fromUser(user);
            result.add(userDtoToAdd);
        }

        return result;
    }
}
