package com.therearenotasksforus.videohostingapi.service;

import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;

import java.util.List;

public interface ProfileService {
    Profile findById(Long id);
    Profile findByCustomUrl(String customUrl);
    Profile findByUser(User user);

    List<Profile> getAll();

    void delete(Long id);

}
