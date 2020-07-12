package com.therearenotasksforus.videohostingapi.service;

import com.therearenotasksforus.videohostingapi.models.User;

import java.util.List;

public interface UserService {
    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);
}
