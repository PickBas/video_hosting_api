package com.saied.videohostingapi.service.impl;

import com.saied.videohostingapi.bucket.BucketName;
import com.saied.videohostingapi.dto.profile.ProfileDto;
import com.saied.videohostingapi.dto.profile.ProfileUpdateDto;
import com.saied.videohostingapi.exceptions.profile.ProfileNotFoundException;
import com.saied.videohostingapi.exceptions.user.AppUserNotFoundException;
import com.saied.videohostingapi.filestore.FileStore;
import com.saied.videohostingapi.models.Channel;
import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.repositories.ProfileRepository;
import com.saied.videohostingapi.service.ChannelService;
import com.saied.videohostingapi.service.ProfileService;
import com.saied.videohostingapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;
import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final ChannelService channelService;
    private final FileStore fileStore;

    @Autowired
    public ProfileServiceImpl(
        ProfileRepository profileRepository,
        UserService userService,
        ChannelService channelService,
        FileStore fileStore
    ) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.channelService = channelService;
        this.fileStore = fileStore;
    }

    @Override
    @Transactional(readOnly = true)
    public Profile findById(Long id) throws ProfileNotFoundException {
        return profileRepository.findById(id).orElseThrow(
            () -> {
                log.warn("Profile with id: {}; does not exist", id);
                return new ProfileNotFoundException(
                    String.format("Could not find profile with id: %s", id)
                );
            }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Profile findByUser(Long userId)
        throws AppUserNotFoundException, ProfileNotFoundException {
        return profileRepository
            .findByUser(this.userService.findById(userId))
            .orElseThrow(
            () -> {
                log.warn("Profile with user who's id: {}; does not exist", userId);
                return new ProfileNotFoundException(
                    String.format("Could not find profile with user who's id: %s", userId)
                );
            }
        );
    }

    @Override
    @Transactional(rollbackFor = AppUserNotFoundException.class)
    public void createProfile(
        Long userId,
        ProfileDto profileCreateDto
    ) throws AppUserNotFoundException {
        String avatarUrl;
        if (profileCreateDto.getAvatarUrl() == null) {
            avatarUrl = Profile.DEFAULT_AVATAR_URL;
        } else {
            avatarUrl = profileCreateDto.getAvatarUrl();
        }
        Profile.builder()
            .user(this.userService.findById(userId))
            .country(profileCreateDto.getCountry())
            .avatarUrl(avatarUrl)
            .build();
    }

    @Override
    @Transactional(
        rollbackFor = {
            ProfileNotFoundException.class,
            ChannelNotFoundException.class
        }
    )
    public void addOwnedChannel(
        Long profileId,
        Long channelId
    ) throws ProfileNotFoundException, ChannelNotFoundException {
        Profile profile = this.findById(profileId);
        Channel channel = this.channelService.findById(channelId);
        profile.addOwnedChannel(channel);
    }

    @Override
    @Transactional(
        rollbackFor = {ProfileNotFoundException.class, ValidationException.class}
    )
    public void update(
        Long profileId,
        ProfileUpdateDto profileUpdateDto
    ) throws ProfileNotFoundException, ValidationException {
        if (profileUpdateDto.getCountry() == null) {
            log.warn("Provided data for update for profile with id: {}; is invalid", profileId);
            throw new ValidationException("Provided profile data for update is invalid");
        }
        Profile profile = this.findById(profileId);
        profile.setCountry(profileUpdateDto.getCountry());
    }

    @Override
    @Transactional(rollbackFor = ProfileNotFoundException.class)
    public void uploadProfileAvatar(
        Long profileId,
        MultipartFile file
    ) throws ProfileNotFoundException {
        Profile profile = this.findById(profileId);
        try {
            this.isEmptyFile(file);
            this.isImage(file);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        String basicUrl = "https://"
                + BucketName.BUCKET.getBucketName()
                + ".s3."
                + BucketName.BUCKET.getBucketRegion()
                + ".amazonaws.com/";
        String originalFileName = Objects
            .requireNonNull(
                file.getOriginalFilename()
            )
            .replaceAll(" ", "_");
        String path = String.format("%s/%s", BucketName.BUCKET.getBucketName(), profileId);
        String filename = String.format("%s-%s", UUID.randomUUID(), originalFileName);
        try {
            fileStore.save(path, filename, Optional.of(getMetadata(file)), file.getInputStream());
            profile.setAvatarUrl(basicUrl + profileId + "/" + filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Checking if file is image
     * @param file File
     */
    private void isImage(MultipartFile file) throws Exception {
        if (
            !Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()
            ).contains(file.getContentType())
        ) {
            throw new Exception("Failure: file must be an image [" + file.getContentType() + "]");
        }
    }

    /**
     * Checking if file is empty
     * @param file File
     */
    private void isEmptyFile(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Failure: cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = ProfileNotFoundException.class)
    public byte[] downloadUserProfileImage(Long profileId) throws ProfileNotFoundException {
        Profile profile = this.findById(profileId);
        String path = String.format(
            "%s/%s",
            BucketName.BUCKET.getBucketName(),
            profileId
        );
        String[] pathArr = profile.getAvatarUrl().split("/");
        String filename = pathArr[pathArr.length - 1];
        return fileStore.download(path, filename);
    }

    /**
     * Getting file's metadata
     * @param file MultipartFile
     * @return HashMap with Content-Type and Content-Length keys
     */
    private Map<String, String> getMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    @Override
    public void delete(Long id) {
        profileRepository.deleteById(id);
    }
}
