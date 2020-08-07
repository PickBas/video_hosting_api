package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.profile.ProfileDto;
import com.therearenotasksforus.videohostingapi.dto.profile.ProfileUpdateDto;
import com.therearenotasksforus.videohostingapi.dto.user.UserDto;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import com.therearenotasksforus.videohostingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.security.Principal;
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
    @CrossOrigin
    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = profileService.getAll();
        List<ProfileDto> result = new ArrayList<>();

        for (Profile profile : profiles) {
            result.add(ProfileDto.fromProfile(profile));
        }

        return result;
    }

    @GetMapping("/api/profile")
    @CrossOrigin
    public ProfileDto getCurrentProfile(Principal principal) {
        try {
            User currentUser = userService.findByUsername(principal.getName());
            Profile currentProfile = currentUser.getProfile();
            return ProfileDto.fromProfile(currentProfile);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/api/profile/id/{id}")
    @CrossOrigin
    public ProfileDto getProfileById(@PathVariable(name = "id") Long id) {
        return ProfileDto.fromProfile(profileService.findById(id));
    }

    @PostMapping("/api/profile/update")
    @CrossOrigin
    public String updateProfile(Principal principal, @RequestBody ProfileUpdateDto requestDto) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();

        try {
            profileService.update(currentProfile, requestDto);
        } catch (ValidationException e) {
            return "Failure: wrong data was provided";
        }

        return "Success: profile " + currentProfile.getUser().getUsername() + " was updated!";
    }

    @PostMapping(
            path = "/api/profile/upload/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    public void uploadProfileAvatar(Principal principal, @RequestParam("file") MultipartFile file){
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();

        profileService.uploadProfileAvatar(currentProfile, file);

    }

}

