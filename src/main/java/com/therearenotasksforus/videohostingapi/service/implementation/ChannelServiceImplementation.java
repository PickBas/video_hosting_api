package com.therearenotasksforus.videohostingapi.service.implementation;

import com.therearenotasksforus.videohostingapi.dto.channel.ChannelCreateDto;
import com.therearenotasksforus.videohostingapi.dto.channel.ChannelUpdateDto;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;
import com.therearenotasksforus.videohostingapi.repositories.ChannelRepository;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import com.therearenotasksforus.videohostingapi.service.ChannelService;
import com.therearenotasksforus.videohostingapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelServiceImplementation implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ProfileRepository profileRepository;
    private final VideoService videoService;

    @Autowired
    public ChannelServiceImplementation(ChannelRepository channelRepository,
                                        ProfileRepository profileRepository,
                                        VideoService videoService) {
        this.channelRepository = channelRepository;
        this.profileRepository = profileRepository;
        this.videoService = videoService;
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
        if (channelUpdateDto.getName() == null && channel.getInfo() == null)
            throw new ValidationException("Wrong data was provided");

        channel.setUpdated(new Timestamp(System.currentTimeMillis()));

        channel.setName(channelUpdateDto.getName() != null ? channelUpdateDto.getName() : "");
        channel.setInfo(channelUpdateDto.getInfo() != null ? channelUpdateDto.getInfo() : "");

        channelRepository.save(channel);
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
