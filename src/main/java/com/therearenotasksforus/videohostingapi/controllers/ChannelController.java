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
import java.security.Principal;
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
    @CrossOrigin
    public String channelCreate(Principal principal, @RequestBody ChannelCreateDto requestDto) {
        Profile channelOwner = userService.findByUsername(principal.getName()).getProfile();

        if (requestDto.getName().isEmpty()) {
            return "Failure: No channel name was provided!";
        }

        Channel channel = channelService.create(channelOwner, requestDto);

        profileService.addOwnedChannel(channelOwner, channel);

        return String.format("Success: Channel %s was created!", channel.getName());
    }

    @GetMapping("/api/channels")
    @CrossOrigin
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
    @CrossOrigin
    public List<Channel> getAllOwnedChannels(Principal principal) {
        Profile channelsOwner;

        try {
            channelsOwner = userService.findByUsername(principal.getName()).getProfile();
        } catch (Exception e) {
            return null;
        }

        return channelService.getAllOwnedChannels(channelsOwner);
    }

    @GetMapping("/api/channel/{id}")
    @CrossOrigin
    public ChannelDto getChannelById(@PathVariable(name = "id") Long id) {
        Channel channel = channelService.findById(id);

        if (channel == null) {
            return null;
        }

        return ChannelDto.fromChannel(channel);
    }

    @PostMapping("/api/channel/{id}/update")
    @CrossOrigin
    public String updateChannel(Principal principal, @PathVariable(name = "id") Long id, @RequestBody ChannelUpdateDto requestDto) {
        Profile ownerProfile = userService.findByUsername(principal.getName()).getProfile();
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

    @PostMapping("/api/channel/{id}/subscribe")
    @CrossOrigin
    public String subscribeToChannel(Principal principal, @PathVariable(name = "id") Long id) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Channel currentChannel = channelService.findById(id);

        try {
            channelService.subscribeToChannel(currentProfile, currentChannel);
        } catch (Exception e) {
            return "Failure: " + e.getMessage();
        }

        return "Success: user " + currentProfile.getUser().getUsername() + " subscribed to " + currentChannel.getName() + " channel!";
    }

}
