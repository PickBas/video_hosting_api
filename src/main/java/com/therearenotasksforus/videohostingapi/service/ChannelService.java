package com.therearenotasksforus.videohostingapi.service;

import com.therearenotasksforus.videohostingapi.dto.ChannelCreateDto;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;

import java.util.List;

public interface ChannelService {
    Channel findById(Long id);

    Channel create(Profile channelOwner, ChannelCreateDto requestDto);

    List<Channel> getAll();

    List<Channel> getAllOwnedChannels(Profile profile);

    void delete(Long id);
}
