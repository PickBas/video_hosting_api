package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.profile.ProfileDto;
import com.saied.videohostingapi.dto.profile.ProfileUpdateDto;
import com.saied.videohostingapi.exceptions.channel.ChannelNotFoundException;
import com.saied.videohostingapi.exceptions.img.ImageFileIsEmptyException;
import com.saied.videohostingapi.exceptions.img.InvalidImageFormatException;
import com.saied.videohostingapi.exceptions.img.InvalidProvidedImageException;
import com.saied.videohostingapi.exceptions.profile.ProfileNotFoundException;
import com.saied.videohostingapi.exceptions.user.AppUserNotFoundException;
import com.saied.videohostingapi.models.Profile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;

public interface ProfileService {

    /**
     * Finding profile by id
     * @param id Profile id
     * @return Profile entity
     */
    Profile findById(Long id) throws ProfileNotFoundException;

    /**
     * Finding profile by user
     * @param userId User id
     * @return Profile entity
     */
    Profile findByUser(Long userId) throws AppUserNotFoundException, ProfileNotFoundException;

    /**
     * Adding a channel to the list of owned channels by provided users
     * @param profileId Profile id
     * @param channelId Channel id
     */
    void addOwnedChannel(Long profileId, Long channelId) throws ProfileNotFoundException, ChannelNotFoundException;

    /**
     * Creating profile for a user
     * @param userId User id
     * @param profileCreateDto Profile data
     */
    void createProfile(Long userId, ProfileDto profileCreateDto) throws AppUserNotFoundException;

    /**
     * Updating profile's data
     * @param profileId Profile id
     * @param profileUpdateDto New profile data
     * @throws ValidationException If provided data is invalid
     */
    void update(Long profileId, ProfileUpdateDto profileUpdateDto) throws ValidationException, ProfileNotFoundException;

    /**
     * Uploading profile's avatar
     * @param profileId Profile id
     * @param file Avatar
     */
    void uploadProfileAvatar(Long profileId, MultipartFile file)
        throws ProfileNotFoundException, ImageFileIsEmptyException, InvalidImageFormatException, InvalidProvidedImageException;

    /**
     * Downloading profile's avatar
     * @param profileId Profile id
     * @return Avatar as byte array
     */
    byte[] downloadUserProfileImage(Long profileId) throws ProfileNotFoundException;

    /**
     * Deleting profile
     * @param id Profile id
     */
    void delete(Long id);
}
