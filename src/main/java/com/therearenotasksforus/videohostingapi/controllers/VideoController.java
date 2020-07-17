package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.video.CommentDto;
import com.therearenotasksforus.videohostingapi.dto.video.NameUpdateDto;
import com.therearenotasksforus.videohostingapi.dto.video.VideoDto;
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
import java.util.ArrayList;
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
    public List<VideoDto> getAllVideos() {
        List<Video> videos = videoService.getAll();
        List<VideoDto> videoDtos = new ArrayList<>();

        for (Video video : videos) {
            videoDtos.add(VideoDto.fromVideo(video));
        }

        return videoDtos;
    }

    @GetMapping("/api/video/{id}")
    @CrossOrigin
    public VideoDto getVideo(@PathVariable(name = "id") Long id) {
        return VideoDto.fromVideo(videoService.findById(id));
    }

    @PostMapping("/api/video/{id}/like")
    @CrossOrigin
    public String setLike(Principal principal, @PathVariable(name = "id") Long id) {
        Video currentVideo = videoService.findById(id);
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();

        videoService.setLike(currentProfile, currentVideo);

        return "Success: user " + principal.getName() + " liked video!";
    }

    @PostMapping("/api/video/{id}/dislike")
    @CrossOrigin
    public String setDislike(Principal principal, @PathVariable(name = "id") Long id) {
        Video currentVideo = videoService.findById(id);
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();

        videoService.setDislike(currentProfile, currentVideo);

        return "Success: user " + principal.getName() + " disliked video!";
    }

    @PostMapping("/api/video/{id}/comment")
    @CrossOrigin
    public String setLike(Principal principal, @PathVariable(name = "id") Long id, @RequestBody CommentDto requestDto) {
        Video currentVideo = videoService.findById(id);
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();

        videoService.saveComment(currentProfile, currentVideo, requestDto.getCommentBody());

        return "Success: user " + principal.getName() + " commented videos!";
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
    public String uploadVideo(Principal principal, @PathVariable(name = "id") Long id, @RequestParam("file") MultipartFile file){
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Channel currentChannel = channelService.findById(id);

        try {
            videoService.uploadVideo(currentProfile, currentChannel, file);
        } catch (IllegalStateException e) {
            return e.getMessage();
        }

        return "Success: video was uploaded!";
    }

    @PostMapping("/api/video/{id}/update/name")
    @CrossOrigin
    public String updateVideoName(Principal principal, @PathVariable(name = "id") Long id, @RequestBody NameUpdateDto requestDto) {
        Video currentVideo = videoService.findById(id);
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();

        try {
            videoService.updateName(currentProfile, currentVideo, requestDto.getName());
        } catch (IllegalStateException e) {
            return e.getMessage();
        }

        return "Success: the video is called " + currentVideo.getName() + " !";
    }

}
