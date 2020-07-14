package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.channel.ChannelCreateDto;
import com.therearenotasksforus.videohostingapi.dto.channel.ChannelDto;
import com.therearenotasksforus.videohostingapi.dto.channel.ChannelUpdateDto;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.service.ChannelService;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import com.therearenotasksforus.videohostingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ChannelController {
    private final ChannelService channelService;
    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    public ChannelController(ChannelService channelService, ProfileService profileService, UserService userService) {
        this.channelService = channelService;
        this.profileService = profileService;
        this.userService = userService;
    }

    @PostMapping("/api/channel/create")
    public String channelCreate(@RequestHeader(name = "Authorization") String jwtToken, @RequestBody ChannelCreateDto requestDto) {
        Profile channelOwner = userService.findByJwtToken(jwtToken.substring(6)).getProfile();

        if (requestDto.getName().isEmpty()) {
            return "Failure: No channel name was provided!";
        }

        Channel channel = channelService.create(channelOwner, requestDto);

        profileService.addOwnedChannel(channelOwner, channel);

        return String.format("Success: Channel %s was created!", channel.getName());
    }

    @GetMapping("/api/channels")
    public List<ChannelDto> getAllChannels() {
        List<Channel> channels = channelService.getAll();

        if (channels == null) {
            return null;
        }

        List<ChannelDto> channelDtos = new ArrayList<>();

        for (Channel channel : channels) {
            channelDtos.add(ChannelDto.fromChannel(channel));
        }

        return channelDtos;
    }

    @GetMapping("/api/channels/owned")
    public List<Channel> getAllOwnedChannels(@RequestHeader(name = "Authorization") String jwtToken) {
        Profile channelsOwner;

        try {
            channelsOwner = userService.findByJwtToken(jwtToken.substring(6)).getProfile();
        } catch (Exception e) {
            return null;
        }

        return channelService.getAllOwnedChannels(channelsOwner);
    }

    @GetMapping("/api/channel/{id}")
    public ChannelDto getChannelById(@PathVariable(name = "id") Long id) {
        Channel channel = channelService.findById(id);

        if (channel == null) {
            return null;
        }

        return ChannelDto.fromChannel(channel);
    }

    @PostMapping("/api/channel/{id}/update")
    public String updateChannel(@RequestHeader(name = "Authorization") String jwtToken, @PathVariable(name = "id") Long id, @RequestBody ChannelUpdateDto requestDto) {
        Profile ownerProfile = userService.findByJwtToken(jwtToken.substring(6)).getProfile();
        Channel channel = channelService.findById(id);

        if (channel == null) {
            return "Failure: No channel with such id found!";
        }

        if (!channelService.isProfileOwner(ownerProfile, channel)) {
            return "Failure: the profile is not the owner of the channel!";
        }

        try {
            channelService.update(channel, requestDto);
        } catch (ValidationException e) {
            return "Failure: wrong data was provided!";
        }

        return "Success: channel " + channel.getName() + " was updated!";

    }

}
