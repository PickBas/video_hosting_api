package com.therearenotasksforus.videohostingapi.service.implementation;

import com.therearenotasksforus.videohostingapi.dto.channel.ChannelCreateDto;
import com.therearenotasksforus.videohostingapi.dto.channel.ChannelUpdateDto;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.repositories.ChannelRepository;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import com.therearenotasksforus.videohostingapi.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelServiceImplementation implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public ChannelServiceImplementation(ChannelRepository channelRepository, ProfileRepository profileRepository) {
        this.channelRepository = channelRepository;
        this.profileRepository = profileRepository;
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
    public void subscribeToChannel(Profile profile, Channel channel) throws Exception {
        if (isProfileOwner(profile, channel)) {
            throw new Exception("The user is the owner of the channel");
        }

        profile.addSubscription(channel);
        profileRepository.save(profile);

        channel.addSubscriber(profile);
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
    public void delete(Long id) {
        channelRepository.findById(id).ifPresent(channel -> channelRepository.deleteById(id));
    }
}
