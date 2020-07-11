package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/profile/add")
    public Profile addProfile(@RequestBody Profile profile) {
        System.out.println(profile);
        profileRepository.save(profile);
        return ;
    }

    @GetMapping("/profiles")
    public Iterable<Profile> profiles() {
        return profileRepository.findAll();
    }
}
