package com.therearenotasksforus.videohostingapi.service.implementation;

import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImplementation implements ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImplementation(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    @Override
    public Profile findById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    @Override
    public Profile findByCustomUrl(String customUrl) {
        return profileRepository.findByCustomUrl(customUrl);
    }

    @Override
    public Profile findByUser(User user) {
        return null;
    }

    @Override
    public List<Profile> getAll() {
        return profileRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        profileRepository.deleteById(id);
    }
}
