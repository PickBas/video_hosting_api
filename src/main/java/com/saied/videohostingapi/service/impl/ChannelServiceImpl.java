package com.saied.videohostingapi.service.impl;

import com.saied.videohostingapi.bucket.BucketName;
import com.saied.videohostingapi.dto.channel.ChannelCreateDto;
import com.saied.videohostingapi.dto.channel.ChannelUpdateDto;
import com.saied.videohostingapi.filestore.FileStore;
import com.saied.videohostingapi.models.Channel;
import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.Video;
import com.saied.videohostingapi.repositories.ChannelRepository;
import com.saied.videohostingapi.repositories.ProfileRepository;
import com.saied.videohostingapi.service.ChannelService;
import com.saied.videohostingapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ProfileRepository profileRepository;
    private final VideoService videoService;
    private final FileStore fileStore;

    @Autowired
    public ChannelServiceImpl(ChannelRepository channelRepository,
                              ProfileRepository profileRepository,
                              VideoService videoService,
                              FileStore fileStore) {
        this.channelRepository = channelRepository;
        this.profileRepository = profileRepository;
        this.videoService = videoService;
        this.fileStore = fileStore;
    }

    @Override
    public Channel findById(Long id) {
        return channelRepository.findById(id).orElse(null);
    }

    @Override
    public Channel create(Profile channelOwner, ChannelCreateDto requestDto) {
        Channel channel = new Channel();
        channel.setName(requestDto.getName());
        channel.setInfo(requestDto.getInfo());
        channel.setOwner(channelOwner);
        channel.setCreated(new Timestamp(System.currentTimeMillis()));
        channel.setUpdated(new Timestamp(System.currentTimeMillis()));
        return channelRepository.save(channel);
    }

    @Override
    public void update(Channel channel, ChannelUpdateDto channelUpdateDto) throws ValidationException {
        isChannelDataValid(channel, channelUpdateDto);
        channel.setUpdated(new Timestamp(System.currentTimeMillis()));
        channel.setName(channelUpdateDto.getName() != null ? channelUpdateDto.getName() : "");
        channel.setInfo(channelUpdateDto.getInfo() != null ? channelUpdateDto.getInfo() : "");
        channelRepository.save(channel);
    }

    private void isChannelDataValid(Channel channel, ChannelUpdateDto channelUpdateDto) throws ValidationException {
        if (channelUpdateDto.getName() == null && channel.getInfo() == null)
            throw new ValidationException("Wrong data was provided");
    }

    @Override
    public boolean isProfileOwner(Profile profile, Channel channel) {
        return profile == channel.getOwner();
    }

    @Override
    public void subscribeToChannel(Profile profile, Channel channel) throws IllegalStateException {
        if (isProfileOwner(profile, channel)) {
            throw new IllegalStateException("The user is the owner of the channel!");
        }
        if (channel.getSubscribers().contains(profile)) {
            throw new IllegalStateException("The user has already subscribed to the channel!");
        }
        profile.addSubscription(channel);
        profileRepository.save(profile);
        channel.addSubscriber(profile);
        channelRepository.save(channel);

    }

    @Override
    public void unsubscribeFromChannel(Profile profile, Channel channel) throws Exception {
        if (!channel.getSubscribers().contains(profile)) {
            throw new Exception("the user did not subscribe to the channel");
        }
        profile.removeSubscription(channel);
        profileRepository.save(profile);
        channel.removeSubscriber(profile);
        channelRepository.save(channel);

    }

    @Override
    public void uploadChannelAvatar(Channel channel, MultipartFile file) {
        isEmptyFile(file);
        isImage(file);
        Map<String, String> uploadPathData = getUploadPathData(channel, file);
        try {
            fileStore.save(uploadPathData.get("path"),
                    uploadPathData.get("filename"),
                    Optional.of(getMetadata(file)),
                    file.getInputStream());
            channel.setAvatarUrl(uploadPathData.get("basicUrl") +
                    channel.getId() +
                    "/" +
                    uploadPathData.get("filename"));
            channelRepository.save(channel);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Map<String, String> getMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("content-length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("Failure: file must be an image [" + file.getContentType() + "]");
        }
    }

    private void isEmptyFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Failure: cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    private Map<String, String> getUploadPathData(Channel channel, MultipartFile file) {
        Map<String, String> uploadPathData = new HashMap<>();
        String basicUrl =
                "https://"
                + BucketName.BUCKET.getBucketName()
                + ".s3."
                + BucketName.BUCKET.getBucketRegion()
                + ".amazonaws.com/";
        uploadPathData.put("basicUrl", basicUrl);
        uploadPathData.put("originalFileName",
                Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", "_"));
        uploadPathData.put("path",
                String.format("%s/%s", BucketName.BUCKET.getBucketName(), channel.getId()));
        uploadPathData.put("filename",
                String.format("%s-%s", UUID.randomUUID(), uploadPathData.get("originalFileName")));
        return uploadPathData;
    }

    @Override
    public List<Channel> getAll() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> getAllOwnedChannels(Profile profile) {
        List<Channel> channels = channelRepository.findAll();
        List<Channel> ownedChannels = new ArrayList<>();
        for (Channel channel : channels) {
            if (channel.getOwner().equals(profile)) {
                ownedChannels.add(channel);
            }
        }
        return ownedChannels;
    }

    @Override
    public byte[] downloadChannelImage(Channel channel) {
        String path = String.format("%s/%s",
                BucketName.BUCKET.getBucketName(),
                channel.getId());
        String[] pathArr = channel.getAvatarUrl().split("/");
        String filename = pathArr[pathArr.length - 1];
        return fileStore.download(path, filename);
    }

    @Override
    public void delete(Channel channel) {
        for (Video video : channel.getVideos())
            videoService.delete(video.getId());
        channel.setSubscribers(new ArrayList<>());
        channelRepository.save(channel);
        List<Channel> ownedChannels = channel.getOwner().getOwnedChannels();
        ownedChannels.remove(channel);
        channel.getOwner().setOwnedChannels(ownedChannels);
        profileRepository.save(channel.getOwner());
        channelRepository.deleteById(channel.getId());
    }

}
