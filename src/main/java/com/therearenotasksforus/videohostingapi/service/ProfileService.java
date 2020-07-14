package com.therearenotasksforus.videohostingapi.service;

import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;

import java.util.List;

public interface ProfileService {
    Profile findById(Long id);
    Profile findByCustomUrl(String customUrl);
    Profile findByUser(User user);

    void addOwnedChannel(Profile profile, Channel channel);

    List<Profile> getAll();

    void delete(Long id);

}