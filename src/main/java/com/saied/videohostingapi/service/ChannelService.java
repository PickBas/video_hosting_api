package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.channel.ChannelCreateDto;
import com.saied.videohostingapi.dto.channel.ChannelUpdateDto;
import com.saied.videohostingapi.models.Channel;
import com.saied.videohostingapi.models.Profile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;
import java.util.List;

public interface ChannelService {

    /**
     * Finding channel by id
     * @param id Channel id
     * @return Channel entity
     */
    Channel findById(Long id);

    /**
     * Creating channel from dto
     * @param channelOwner Profile entity - channel owner
     * @param requestDto New channel data
     * @return Channel entity
     */
    Channel create(Profile channelOwner, ChannelCreateDto requestDto);

    /**
     * Updating channel's data
     * @param channel Channel entity
     * @param channelUpdateDto New channel data
     * @throws ValidationException If provided data is incorrect
     */
    void update(
        Channel channel,
        ChannelUpdateDto channelUpdateDto
    ) throws ValidationException;

    /**
     * Checking if provided user is the owner of the channel
     * @param profile Profile entity
     * @param channel Channel entity
     * @return true if user is owner, false otherwise
     */
    boolean isProfileOwner(Profile profile, Channel channel);

    /**
     * Subscribing provided user to provided channel
     * @param profile Profile entity
     * @param channel Channel entity
     * @throws Exception If user is already subscribed to the channel
     */
    void subscribeToChannel(Profile profile, Channel channel) throws Exception;

    /**
     * Subscribing provided user to provided channel
     * @param profile Profile entity
     * @param channel Channel entity
     * @throws Exception If user is not subscribed to the channel
     */
    void unsubscribeFromChannel(Profile profile, Channel channel) throws Exception;

    /**
     * Uploading avatar on provided channel
     * @param channel Channel entity
     * @param file Avatar
     */
    void uploadChannelAvatar(Channel channel, MultipartFile file);

    /**
     * Listing all channels with pagination
     * @param page Number of the page
     * @return List of Channel entities
     */
    List<Channel> getChannelsPaginated(int page);

    /**
     * Listing all channels owned by provided user with pagination
     * @param profile Profile entity
     * @param page Number of the page
     * @return List of Channel entities
     */
    List<Channel> getOwnedChannelsPaginated(Profile profile, int page);

    /**
     * Deleting channel
     * @param channel Channel entity
     */
    void delete(Channel channel);

    /**
     * Downloading channel's avatar
     * @param channel Channel Entity
     * @return Avatar in array of bytes
     */
    byte[] downloadChannelImage(Channel channel);
}
