package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.profile.ProfileDto;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/api/profiles")
    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = profileService.getAll();
        List<ProfileDto> result = new ArrayList<>();

        for (Profile profile : profiles) {
            result.add(ProfileDto.fromUser(profile));
        }

        return result;
    }

}
