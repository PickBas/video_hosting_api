package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.profile.ProfileDto;
import com.therearenotasksforus.videohostingapi.dto.profile.ProfileUpdateDto;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import com.therearenotasksforus.videohostingapi.service.UserService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor @Slf4j
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping("/api/profiles")
    @CrossOrigin
    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = profileService.getAll();
        List<ProfileDto> result = new ArrayList<>();
        for (Profile profile : profiles) {
            result.add(ProfileDto.fromProfile(profile));
        }
        log.info("Loaded all profiles. HttpStatus: {}", HttpStatus.OK);
        return result;
    }

    @GetMapping("/api/profile")
    @CrossOrigin
    public ResponseEntity<ProfileDto> getCurrentProfile(Principal principal) {
        try {
            User currentUser = userService.findByUsername(principal.getName());
            Profile currentProfile = currentUser.getProfile();
            log.info("Loaded current profile of user {}. HttpStatus: {}", principal.getName(), HttpStatus.OK);
            return ResponseEntity.ok().body(ProfileDto.fromProfile(currentProfile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/api/profile/id/{id}")
    @CrossOrigin
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable(name = "id") Long id) {
        Profile profile = profileService.findById(id);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        log.info("Loaded profile with id {}. HttpStatus: {}", id, HttpStatus.OK);
        return ResponseEntity.ok().body(ProfileDto.fromProfile(profileService.findById(id)));
    }

    @PostMapping("/api/profile/update")
    @CrossOrigin
    public ResponseEntity<?> updateProfile(Principal principal,
            @RequestBody ProfileUpdateDto requestDto) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Map<String, String> response = new HashMap<>();
        try {
            profileService.update(currentProfile, requestDto);
        } catch (ValidationException e) {
            response.put("Error", "Wrong data was provided!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("Success", "profile " + currentProfile.getUser().getUsername() + " was updated!");
        log.info("Updated profile of user {}. HttpStatus: {}",
                currentProfile.getUser().getUsername(), HttpStatus.OK);
        return ResponseEntity.ok(ProfileDto.fromProfile(profileService.findById(currentProfile.getId())));
    }

    @PostMapping(
            path = "/api/profile/upload/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    public ResponseEntity<?> uploadProfileAvatar(Principal principal,
                                                 @RequestParam("file") MultipartFile file) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        profileService.uploadProfileAvatar(currentProfile, file);
        log.info("Uploaded profile avatar. HttpStatus: {}", HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/profile/{id}/download/avatar")
    public byte[] downloadUserProfileImage(@PathVariable("id") Long id) {
        byte[] image = profileService.downloadUserProfileImage(profileService.findById(id));
        log.info("Downloaded profile avatar. HttpStatus: {}", HttpStatus.OK);
        return image;
    }

}

