package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.profile.ProfileUpdateDto;
import com.saied.videohostingapi.models.Channel;
import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.User;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;

public interface ProfileService {
    Profile findById(Long id);
    Profile findByCustomUrl(String customUrl);
    Profile findByUser(User user);
    void addOwnedChannel(Profile profile, Channel channel);
    void update(Profile profile, ProfileUpdateDto profileUpdateDto) throws ValidationException;
    void uploadProfileAvatar(Profile profile, MultipartFile file);
    byte[] downloadUserProfileImage(Profile profile);
    void delete(Long id);
    void deleteLikedVideoById(Profile profile, Long id);
}
