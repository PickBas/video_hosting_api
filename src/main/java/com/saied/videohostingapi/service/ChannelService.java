package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.channel.ChannelCreateDto;
import com.saied.videohostingapi.dto.channel.ChannelUpdateDto;
import com.saied.videohostingapi.models.Channel;
import com.saied.videohostingapi.models.Profile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;
import java.util.List;

public interface ChannelService {
    Channel findById(Long id);
    Channel create(Profile channelOwner, ChannelCreateDto requestDto);
    void update(Channel channel, ChannelUpdateDto channelUpdateDto) throws ValidationException;
    boolean isProfileOwner(Profile profile, Channel channel);
    void subscribeToChannel(Profile profile, Channel channel) throws Exception;
    void unsubscribeFromChannel(Profile profile, Channel channel) throws Exception;
    void uploadChannelAvatar(Channel channel, MultipartFile file);
    List<Channel> getAll();
    List<Channel> getAllOwnedChannels(Profile profile);
    void delete(Channel channel);
    byte[] downloadChannelImage(Channel channel);
}
