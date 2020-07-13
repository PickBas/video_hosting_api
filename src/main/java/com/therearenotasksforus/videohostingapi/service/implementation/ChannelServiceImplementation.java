package com.therearenotasksforus.videohostingapi.service.implementation;

import com.therearenotasksforus.videohostingapi.dto.ChannelCreateDto;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.repositories.ChannelRepository;
import com.therearenotasksforus.videohostingapi.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelServiceImplementation implements ChannelService {

    private final ChannelRepository channelRepository;

    @Autowired
    public ChannelServiceImplementation(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
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
