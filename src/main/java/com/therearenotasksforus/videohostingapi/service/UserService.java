package com.therearenotasksforus.videohostingapi.service;

import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;

import java.util.List;

public interface UserService {
    User register(User user);

    void updateUserToken(User user, String jwtToken);

    void updateNames(User user, String firstName, String lastName);

    List<User> getAll();

    User findById(Long id);
    User findByProfile(Profile profile);
    User findByJwtToken(String jwtToken);
    User findByUsername(String username);

    void delete(Long id);
}
