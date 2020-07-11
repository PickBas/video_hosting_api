package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/profile/add")
    public String addProfile(@RequestBody Profile profile) {
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            return "Failure: the profile already exists";
        }
        return "Success";
    }

    @GetMapping("/profiles")
    public Iterable<Profile> profiles() {
        return profileRepository.findAll();
    }
}
