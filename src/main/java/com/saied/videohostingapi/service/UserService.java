package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.user.UpdateUserDto;
import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.User;

import jakarta.validation.ValidationException;
import java.util.List;

public interface UserService {
    void register(User user);
    void checkIfUserExists(User user);
    void updatePassword(User user, String password);
    void updateNames(User user, UpdateUserDto updateUserDto) throws ValidationException;
    List<User> getAll();
    User findById(Long id);
    User findByProfile(Profile profile);
    User findByUsername(String username);
    void delete(Long id);
}
