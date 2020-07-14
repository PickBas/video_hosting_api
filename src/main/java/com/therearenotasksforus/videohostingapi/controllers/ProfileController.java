package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.profile.ProfileDto;
import com.therearenotasksforus.videohostingapi.dto.user.UserDto;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import com.therearenotasksforus.videohostingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProfileController {
    private final ProfileService profileService;

    private final UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("/api/profiles")
    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = profileService.getAll();
        List<ProfileDto> result = new ArrayList<>();

        for (Profile profile : profiles) {
            result.add(ProfileDto.fromProfile(profile));
        }

        return result;
    }

    @GetMapping("/api/profile")
    public ProfileDto getCurrentProfile(@RequestHeader(name = "Authorization") String jwtToken) {
        try {
            User currentUser = userService.findByJwtToken(jwtToken.substring(6));
            Profile currentProfile = currentUser.getProfile();
            return ProfileDto.fromProfile(currentProfile);
        } catch (Exception e) {
            return null;
        }
    }

}
