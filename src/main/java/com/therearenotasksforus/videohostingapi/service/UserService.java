package com.therearenotasksforus.videohostingapi.service;

import com.therearenotasksforus.videohostingapi.dto.user.UpdateUserDto;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;

import javax.xml.bind.ValidationException;
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
