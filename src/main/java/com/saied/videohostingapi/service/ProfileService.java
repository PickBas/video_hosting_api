package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.profile.ProfileUpdateDto;
import com.saied.videohostingapi.models.Profile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;

public interface ProfileService {

    /**
     * Finding profile by id
     * @param id Profile id
     * @return Profile entity
     */
    Profile findById(Long id);

    /**
     * Finding profile by user
     * @param userId User id
     * @return Profile entity
     */
    Profile findByUser(Long userId);

    /**
     * Adding a channel to the list of owned channels by provided users
     * @param profileId Profile id
     * @param channelId Channel id
     */
    void addOwnedChannel(Long profileId, Long channelId);

    /**
     * Updating profile's data
     * @param profileId Profile id
     * @param profileUpdateDto New profile data
     * @throws ValidationException If provided data is invalid
     */
    void update(Long profileId, ProfileUpdateDto profileUpdateDto) throws ValidationException;

    /**
     * Uploading profile's avatar
     * @param profileId Profile id
     * @param file Avatar
     */
    void uploadProfileAvatar(Long profileId, MultipartFile file);

    /**
     * Downloading profile's avatar
     * @param profileId Profile id
     * @return Avatar as byte array
     */
    byte[] downloadUserProfileImage(Long profileId);

    /**
     * Deleting profile
     * @param id Profile id
     */
    void delete(Long id);
}
