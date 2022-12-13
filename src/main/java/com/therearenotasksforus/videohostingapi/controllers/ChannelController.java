package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.channel.ChannelCreateDto;
import com.therearenotasksforus.videohostingapi.dto.channel.ChannelDto;
import com.therearenotasksforus.videohostingapi.dto.channel.ChannelUpdateDto;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.service.ChannelService;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import com.therearenotasksforus.videohostingapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ValidationException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor @Slf4j
public class ChannelController {
    private final ChannelService channelService;
    private final ProfileService profileService;
    private final UserService userService;

    @PostMapping("/api/channel/create")
    @CrossOrigin
    public ResponseEntity<?> channelCreate(Principal principal, @RequestBody ChannelCreateDto requestDto) {
        Map<String, String> response = new HashMap<>();
        Profile channelOwner = userService.findByUsername(principal.getName()).getProfile();
        if (requestDto.getName().isEmpty()) {
            response.put("Error", "No channel name was provided!");
            return ResponseEntity.badRequest().body(response);
        }
        Channel channel = channelService.create(channelOwner, requestDto);
        profileService.addOwnedChannel(channelOwner, channel);
        log.info("Channel with id {} is created. HttpStatus: {}", channel.getId(), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(ChannelDto.fromChannel(channel));
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
        log.info("All channels loaded. HttpStatus: {}", HttpStatus.OK);
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
        log.info("Loaded channels of user {}. HttpStatus: {}", principal.getName(), HttpStatus.OK);
        return channelService.getAllOwnedChannels(channelsOwner);
    }

    @GetMapping("/api/channel/{id}")
    @CrossOrigin
    public ResponseEntity<?> getChannelById(@PathVariable(name = "id") Long id) {
        Channel channel = channelService.findById(id);
        if (channel == null) {
            Map <String, String> response = new HashMap<>();
            response.put("Error", "Cannot find the channel!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        log.info("Loaded channel with id {}. HttpStatus: {}", id, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(ChannelDto.fromChannel(channel));
    }

    @PostMapping("/api/channel/{id}/update")
    @CrossOrigin
    public ResponseEntity<?> updateChannel(Principal principal,
                                           @PathVariable(name = "id") Long id,
                                           @RequestBody ChannelUpdateDto requestDto) {
        Profile ownerProfile = userService.findByUsername(principal.getName()).getProfile();
        Channel channel = channelService.findById(id);
        Map<String, String> response = new HashMap<>();
        if (channel == null) {
            response.put("Error", "No channel with such id found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (!channelService.isProfileOwner(ownerProfile, channel)) {
            response.put("Error", "The profile is not the owner of the channel!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        try {
            channelService.update(channel, requestDto);
        } catch (ValidationException e) {
            response.put("Error", "Wrong data was provided!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        log.info("Channel with id {} was updated. HttpStatus: {}", channel.getId(), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(ChannelDto.fromChannel(channelService.findById(id)));
    }

    @PostMapping("/api/channel/{id}/subscribe")
    @CrossOrigin
    public ResponseEntity<?> subscribeToChannel(Principal principal,
                                                                  @PathVariable(name = "id") Long id) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Channel currentChannel = channelService.findById(id);
        Map<String, String> response = new HashMap<>();
        try {
            channelService.subscribeToChannel(currentProfile, currentChannel);
        } catch (Exception e) {
            response.put("Error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        log.info("User {} subscribed to channel with id {}. HttpStatus: {}",
                currentProfile.getUser().getUsername(), currentChannel.getId(), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/api/channel/{id}/unsubscribe")
    @CrossOrigin
    public ResponseEntity<?> unsubscribeFromChannel(Principal principal, @PathVariable(name = "id") Long id) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Channel currentChannel = channelService.findById(id);
        Map<String, String> response = new HashMap<>();
        try {
            channelService.unsubscribeFromChannel(currentProfile, currentChannel);
        } catch (Exception e) {
            response.put("Error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        response.put("Success", "User " + currentProfile.getUser().getUsername() +
                " unsubscribed from " + currentChannel.getName() + " channel!");
        log.info("User {} unsubscribed from channel with id {}. HttpStatus: {}",
                currentProfile.getUser().getUsername(), currentChannel.getId(), HttpStatus.OK);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/api/channel/{id}")
    @CrossOrigin
    public ResponseEntity<?> deleteChannelById(Principal principal,
                                               @PathVariable(name = "id") Long id) {
        Channel channel = channelService.findById(id);
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        if (channel == null) {
            Map <String, String> response = new HashMap<>();
            response.put("Error", "Cannot find the channel!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (currentProfile != channel.getOwner()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        channelService.delete(channel);
        log.info("Channel with id {} was deleted. HttpStatus: {}", channel.getId(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(
            path = "/api/channel/{id}/upload/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    public ResponseEntity<?> uploadChannelAvatar(Principal principal,
                                                 @RequestParam("file") MultipartFile file,
                                                 @PathVariable("id") Long id) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Channel currentChannel = channelService.findById(id);
        if (currentChannel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (currentChannel.getOwner() != currentProfile) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            channelService.uploadChannelAvatar(currentChannel, file);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("Uploaded image of channel with id {}. HttpStatus: {}", currentChannel.getId(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/api/channel/{id}/download/avatar")
    public byte[] downloadChannelImage(@PathVariable("id") Long id) {
        log.info("Downloaded image of channel with id {}. HttpStatus: {}", id, HttpStatus.OK);
        return channelService.downloadChannelImage(channelService.findById(id));
    }

}
