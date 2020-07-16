package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;
import com.therearenotasksforus.videohostingapi.service.ChannelService;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import com.therearenotasksforus.videohostingapi.service.UserService;
import com.therearenotasksforus.videohostingapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
public class VideoController {
    private final VideoService videoService;
    private final ChannelService channelService;
    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    public VideoController(VideoService videoService, ChannelService channelService, ProfileService profileService, UserService userService) {
        this.videoService = videoService;
        this.channelService = channelService;
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("/api/videos")
    @CrossOrigin
    public List<Video> getAllVideos() {
        return videoService.getAll();
    }

    @GetMapping("/api/channels/{id}/videos")
    @CrossOrigin
    public List<Video> getAllChannelVideos(@PathVariable(name = "id") Long id) {
        Channel channel = channelService.findById(id);
        return channel.getVideos();
    }

    @GetMapping("/api/profiles/{id}/likedvideos")
    @CrossOrigin
    public List<Video> getAllLikedVideos(@PathVariable(name = "id") Long id) {
        Profile profile = profileService.findById(id);
        return profile.getLikedVideos();
    }

    @PostMapping(
            path = "/api/channel/{id}/upload/video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    public void uploadVideo(Principal principal, @PathVariable(name = "id") Long id, @RequestParam("file") MultipartFile file){
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Channel currentChannel = channelService.findById(id);

        videoService.uploadVideo(currentProfile, currentChannel, file);

    }
}
