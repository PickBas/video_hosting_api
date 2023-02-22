package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.channel.ChannelCreateDto;
import com.saied.videohostingapi.dto.channel.ChannelUpdateDto;
import com.saied.videohostingapi.models.Channel;
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
     * @param channelOwnerId Profile id - channel owner
     * @param requestDto New channel data
     * @return Channel entity
     */
    Channel create(Long channelOwnerId, ChannelCreateDto requestDto);

    /**
     * Updating channel's data
     * @param channelId Channel id
     * @param channelUpdateDto New channel data
     * @throws ValidationException If provided data is incorrect
     */
    void update(
        Long channelId,
        ChannelUpdateDto channelUpdateDto
    ) throws ValidationException;

    /**
     * Checking if provided user is the owner of the channel
     * @param profileId Profile id
     * @param channelId Channel id
     * @return true if user is owner, false otherwise
     */
    boolean isProfileOwner(Long profileId, Long channelId);

    /**
     * Subscribing provided user to provided channel
     * @param profileId Profile id
     * @param channelId Channel id
     * @throws Exception If user is already subscribed to the channel
     */
    void subscribeToChannel(Long profileId, Long channelId) throws Exception;

    /**
     * Subscribing provided user to provided channel
     * @param profileId Profile id
     * @param channelId Channel id
     * @throws Exception If user is not subscribed to the channel
     */
    void unsubscribeFromChannel(Long profileId, Long channelId) throws Exception;

    /**
     * Uploading avatar on provided channel
     * @param channelId Channel id
     * @param file Avatar
     */
    void uploadChannelAvatar(Channel channelId, MultipartFile file);

    /**
     * Listing all channels with pagination
     * @param page Number of the page
     * @return List of Channel entities
     */
    List<Channel> getChannelsPaginated(int page);

    /**
     * Listing all channels owned by provided user with pagination
     * @param profileId Profile entity
     * @param page Number of the page
     * @return List of Channel entities
     */
    List<Channel> getOwnedChannelsPaginated(Long profileId, int page);

    /**
     * Deleting channel
     * @param channelId Channel id
     */
    void delete(Long channelId);

    /**
     * Downloading channel's avatar
     * @param channelId Channel Entity
     * @return Avatar in array of bytes
     */
    byte[] downloadChannelImage(Long channelId);
}
