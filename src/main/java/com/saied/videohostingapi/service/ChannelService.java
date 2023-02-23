package com.saied.videohostingapi.service;

import com.saied.videohostingapi.dto.channel.ChannelCreateDto;
import com.saied.videohostingapi.dto.channel.ChannelUpdateDto;
import com.saied.videohostingapi.exceptions.channel.ChannelNotFoundException;
import com.saied.videohostingapi.exceptions.channel.UserIsAlreadySubscribedException;
import com.saied.videohostingapi.exceptions.channel.UserIsNotChannelOwnerException;
import com.saied.videohostingapi.exceptions.channel.UserWasNotSubscribedException;
import com.saied.videohostingapi.exceptions.img.InvalidProvidedImageException;
import com.saied.videohostingapi.exceptions.profile.ProfileNotFoundException;
import com.saied.videohostingapi.models.Channel;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;
import java.util.List;

public interface ChannelService {

    /**
     * Finding channel by id
     * @param id Channel id
     * @return Channel entity
     */
    Channel findById(Long id) throws ChannelNotFoundException;

    /**
     * Creating channel from dto
     * @param channelOwnerId Profile id - channel owner
     * @param requestDto New channel data
     * @return Channel entity
     */
    Channel create(Long channelOwnerId, ChannelCreateDto requestDto) throws ProfileNotFoundException;

    /**
     * Updating channel's data
     * @param profileId Profile id (checking if authorized)
     * @param channelId Channel id
     * @param channelUpdateDto New channel data
     * @throws ValidationException If provided data is incorrect
     */
    void update(
        Long profileId,
        Long channelId,
        ChannelUpdateDto channelUpdateDto
    ) throws ValidationException, ChannelNotFoundException, ProfileNotFoundException, UserIsNotChannelOwnerException;

    /**
     * Checking if provided user is the owner of the channel
     * @param profileId Profile id
     * @param channel Channel entity
     * @throws UserIsNotChannelOwnerException if user is not owner of the channel
     * @throws ProfileNotFoundException if could not find profile
     * @throws ChannelNotFoundException if could not find channel
     */
    void isProfileOwner(
        Long profileId,
        Channel channel
    ) throws UserIsNotChannelOwnerException, ProfileNotFoundException, ChannelNotFoundException;

    /**
     * Subscribing provided user to provided channel
     * @param profileId Profile id
     * @param channelId Channel id
     * @throws ProfileNotFoundException If coudld not fid profile
     * @throws ChannelNotFoundException If could not find channel
     * @throws UserIsAlreadySubscribedException If user is already subscribed to the channel
     */
    void subscribeToChannel(
        Long profileId,
        Long channelId
    ) throws ProfileNotFoundException, ChannelNotFoundException, UserIsAlreadySubscribedException;

    /**
     * Subscribing provided user to provided channel
     * @param profileId Profile id
     * @param channelId Channel id
     * @throws Exception If user is not subscribed to the channel
     */
    void unsubscribeFromChannel(Long profileId, Long channelId)
        throws ProfileNotFoundException, ChannelNotFoundException, UserWasNotSubscribedException;

    /**
     * Uploading avatar on provided channel
     * @param profileId Profile id (checking if authorized)
     * @param channelId Channel id
     * @param file Avatar
     */
    void uploadChannelAvatar(
        Long profileId,
        Long channelId,
        MultipartFile file) throws InvalidProvidedImageException, ChannelNotFoundException, UserIsNotChannelOwnerException;

    /**
     * Listing all channels with pagination
     * @param offset offset
     * @param pageSize number of items
     * @return List of Channel entities
     */
    Page<Channel> getChannelsPaginated(int offset, int pageSize);

    /**
     * Listing all channels owned by provided user with pagination
     * @param profileId Profile entity
     * @param offset offset
     * @param pageSize number of items
     * @return List of Channel entities
     */
    Page<Channel> getOwnedChannelsPaginated(Long profileId, int offset, int pageSize);

    /**
     * Deleting channel
     * @param profileId Profile id (checking if authorized)
     * @param channelId Channel id
     */
    void delete(Long profileId,Long channelId) throws ChannelNotFoundException, UserIsNotChannelOwnerException;

    /**
     * Downloading channel's avatar
     * @param channelId Channel Entity
     * @return Avatar in array of bytes
     */
    byte[] downloadChannelImage(Long channelId) throws ChannelNotFoundException;
}
