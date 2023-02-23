package com.saied.videohostingapi.service.impl;

import com.saied.videohostingapi.bucket.BucketName;
import com.saied.videohostingapi.dto.channel.ChannelCreateDto;
import com.saied.videohostingapi.dto.channel.ChannelUpdateDto;
import com.saied.videohostingapi.exceptions.channel.ChannelNotFoundException;
import com.saied.videohostingapi.exceptions.channel.UserIsAlreadySubscribedException;
import com.saied.videohostingapi.exceptions.channel.UserIsNotChannelOwnerException;
import com.saied.videohostingapi.exceptions.channel.UserWasNotSubscribedException;
import com.saied.videohostingapi.exceptions.img.InvalidProvidedImageException;
import com.saied.videohostingapi.exceptions.profile.ProfileNotFoundException;
import com.saied.videohostingapi.filestore.FileStore;
import com.saied.videohostingapi.models.Channel;
import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.repositories.ChannelRepository;
import com.saied.videohostingapi.service.ChannelService;
import com.saied.videohostingapi.service.ImgValidatorService;
import com.saied.videohostingapi.service.ProfileService;
import com.saied.videohostingapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ProfileService profileService;
    private final VideoService videoService;
    private final ImgValidatorService imgValidatorService;
    private final FileStore fileStore;

    @Autowired
    public ChannelServiceImpl(
        ChannelRepository channelRepository,
        ProfileService profileService,
        VideoService videoService,
        ImgValidatorService imgValidatorService,
        FileStore fileStore
    ) {
        this.channelRepository = channelRepository;
        this.profileService = profileService;
        this.videoService = videoService;
        this.imgValidatorService = imgValidatorService;
        this.fileStore = fileStore;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = ChannelNotFoundException.class)
    public Channel findById(Long id) throws ChannelNotFoundException {
        return channelRepository
            .findById(id)
            .orElseThrow(
                () -> {
                    log.warn("Channel with id: {}; does not exist", id);
                    return new ChannelNotFoundException(
                        String.format("Could not find channel with id: %s", id)
                    );
                }
            );
    }

    @Override
    @Transactional(rollbackFor = ProfileNotFoundException.class)
    public Channel create(
        Long channelOwnerId,
        ChannelCreateDto requestDto
    ) throws ProfileNotFoundException {
        Profile channelOwner = this.profileService.findById(channelOwnerId);
        return Channel.builder()
            .name(requestDto.getName())
            .info(requestDto.getInfo())
            .owner(channelOwner)
            .build();
    }

    @Override
    @Transactional(
        rollbackFor = {
            ValidationException.class,
            ChannelNotFoundException.class,
            UserIsNotChannelOwnerException.class
        }
    )
    public void update(
        Long profileId,
        Long channelId,
        ChannelUpdateDto channelUpdateDto
    ) throws ValidationException, ChannelNotFoundException, UserIsNotChannelOwnerException {
        Channel channel = this.findById(channelId);
        this.isProfileOwner(profileId, channel);
        this.isChannelDataValid(channelUpdateDto);
        if (channelUpdateDto.getName() != null) {
            channel.setName(channelUpdateDto.getName());
        }
        if (channelUpdateDto.getInfo() != null) {
            channel.setInfo(channelUpdateDto.getInfo());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void isProfileOwner(Long profileId, Channel channel)
        throws UserIsNotChannelOwnerException {
        if (!channel.getOwner().getId().equals(profileId)) {
            throw new UserIsNotChannelOwnerException(
                String.format(
                    "Profile with id: %s; is not owner of channel with id: %s. Action unauthorized",
                    profileId,
                    channel.getId()
                )
            );
        }
    }

    @Override
    @Transactional(
        rollbackFor = {
            ProfileNotFoundException.class,
            ChannelNotFoundException.class,
            UserIsAlreadySubscribedException.class
        }
    )
    public void subscribeToChannel(
        Long profileId,
        Long channelId
    ) throws
        ProfileNotFoundException,
        ChannelNotFoundException,
        UserIsAlreadySubscribedException {
        Profile profile = this.profileService.findById(profileId);
        Channel channel = this.findById(channelId);
        profile.addSub(channel);
    }

    @Override
    @Transactional(
        rollbackFor = {
            ProfileNotFoundException.class,
            ChannelNotFoundException.class,
            UserWasNotSubscribedException.class
        }
    )
    public void unsubscribeFromChannel(
        Long profileId,
        Long channelId
    ) throws
        ProfileNotFoundException,
        ChannelNotFoundException,
        UserWasNotSubscribedException {
        Profile profile = this.profileService.findById(profileId);
        Channel channel = this.findById(channelId);
        profile.removeSub(channel);
    }

    @Override
    @Transactional(rollbackFor = {
        InvalidProvidedImageException.class,
        ChannelNotFoundException.class,
        UserIsNotChannelOwnerException.class,
    })
    public void uploadChannelAvatar(
        Long profileId,
        Long channelId,
        MultipartFile file
    ) throws InvalidProvidedImageException, ChannelNotFoundException, UserIsNotChannelOwnerException {
        this.imgValidatorService.isValidImage(file);
        Channel channel = this.findById(channelId);
        this.isProfileOwner(profileId, channel);
        Map<String, String> uploadPathData = getUploadPathData(channelId, file);
        try {
            fileStore.save(
                uploadPathData.get("path"),
                uploadPathData.get("filename"),
                Optional.of(getMetadata(file)),
                file.getInputStream()
            );
            channel.setAvatarUrl(
                uploadPathData.get("basicUrl") +
                channel.getId() +
                "/" +
                uploadPathData.get("filename")
            );
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Channel> getChannelsPaginated(int offset, int pageSize) {
        return this.channelRepository.findAll(PageRequest.of(offset, pageSize));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Channel> getOwnedChannelsPaginated(
        Long profileId,
        int offset,
        int pageSize
    ) {
        return this.channelRepository.findAllByOwnerId(profileId, PageRequest.of(offset, pageSize))
    }

    @Override
    @Transactional(rollbackFor = {
            ChannelNotFoundException.class,
            UserIsNotChannelOwnerException.class,
        }
    )
    public void delete(
        Long profileId,
        Long channelId
    ) throws ChannelNotFoundException, UserIsNotChannelOwnerException {
        Channel channel = this.findById(channelId);
        this.isProfileOwner(profileId, channel);
        this.channelRepository.delete(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadChannelImage(Long channelId) throws ChannelNotFoundException {
        Channel channel = this.findById(channelId);
        String path = String.format("%s/%s",
            BucketName.BUCKET.getBucketName(),
            channelId);
        String[] pathArr = channel.getAvatarUrl().split("/");
        String filename = pathArr[pathArr.length - 1];
        return fileStore.download(path, filename);
    }

    /**
     * Checking if provided data is valid for updating channel's info
     * @param channelUpdateDto Requested data to update
     * @throws ValidationException if data is invalid
     */
    private void isChannelDataValid(ChannelUpdateDto channelUpdateDto) throws ValidationException {
        if (channelUpdateDto.getName() == null && channelUpdateDto.getInfo() == null) {
            throw new ValidationException("Provided channelUpdateDto is invalid");
        }
    }

    /**
     * Getting metadata of an avatar
     * @param file MultipartFile
     * @return  HashMap with `Content-Type` and `Content-Length` keys
     */
    private Map<String, String> getMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    /**
     * Gitting upload path for image
     * @param channelId Channel id
     * @param file MultipartFile
     * @return HashMap with `basicUrl`, `originalFileName`. `path` and `filename` keys
     */
    private Map<String, String> getUploadPathData(Long channelId, MultipartFile file) {
        Map<String, String> uploadPathData = new HashMap<>();
        String basicUrl =
                "https://"
                + BucketName.BUCKET.getBucketName()
                + ".s3."
                + BucketName.BUCKET.getBucketRegion()
                + ".amazonaws.com/";
        uploadPathData.put("basicUrl", basicUrl);
        uploadPathData.put(
            "originalFileName",
            Objects
                .requireNonNull(file.getOriginalFilename())
                .replaceAll(" ", "_")
        );
        uploadPathData.put(
            "path",
            String.format("%s/%s", BucketName.BUCKET.getBucketName(), channelId)
        );
        uploadPathData.put(
            "filename",
            String.format("%s-%s", UUID.randomUUID(), uploadPathData.get("originalFileName"))
        );
        return uploadPathData;
    }

}
