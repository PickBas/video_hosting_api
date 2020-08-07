package com.therearenotasksforus.videohostingapi.security.jwt.services;

import java.util.List;

public interface InvalidJwtsService {
    String findById(Long id);

    List<String> getAll();

    void addNew(String token);

    boolean findByToken(String token);

    void delete(Long id);
}
