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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<VideoDto> getVideo(@PathVariable(name = "id") Long id) {
        Video video = videoService.findById(id);

        return video == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.ok(VideoDto.fromVideo(video));
    }

    @PostMapping("/api/video/{id}/like")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> setLike(Principal principal, @PathVariable(name = "id") Long id) {
        Video currentVideo = videoService.findById(id);

        if (currentVideo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Map<String, String> response = new HashMap<>();

        videoService.setLike(currentProfile, currentVideo);

        response.put("Success", "User " + principal.getName() + " liked video!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/video/{id}/dislike")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> setDislike(Principal principal, @PathVariable(name = "id") Long id) {
        Video currentVideo = videoService.findById(id);
        Map<String, String> response = new HashMap<>();

        if (currentVideo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();

        videoService.setDislike(currentProfile, currentVideo);
        response.put("Success", "User " + principal.getName() + " disliked video!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/video/{id}/comment")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> comment(Principal principal, @PathVariable(name = "id") Long id, @RequestBody CommentDto requestDto) {
        Video currentVideo = videoService.findById(id);

        if (currentVideo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Map<String, String> response = new HashMap<>();
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();

        videoService.saveComment(currentProfile, currentVideo, requestDto.getCommentBody());

        response.put("Success", "User " + principal.getName() + " commented videos!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/channel/{id}/videos")
    @CrossOrigin
    public ResponseEntity<List<Video>> getAllChannelVideos(@PathVariable(name = "id") Long id) {
        Channel channel = channelService.findById(id);
        return channel == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.ok(channel.getVideos());
    }

    @GetMapping("/api/profiles/{id}/likedvideos")
    @CrossOrigin
    public ResponseEntity<List<Video>> getAllLikedVideos(@PathVariable(name = "id") Long id) {
        Profile profile = profileService.findById(id);
        return profile == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.ok(profile.getLikedVideos());
    }

    @PostMapping(
            path = "/api/channel/{id}/upload/video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    public ResponseEntity<?> uploadVideo(
            Principal principal,
            @PathVariable(name = "id") Long id,
            @RequestParam("file") MultipartFile file
    ) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Channel currentChannel = channelService.findById(id);

        Long newVideoId;

        try {
            newVideoId = videoService.uploadVideo(currentProfile, currentChannel, file);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>() {{
                put("Error", e.getMessage());
            }});
        }

        return ResponseEntity.ok(VideoDto.fromVideo(videoService.findById(newVideoId)));
    }

    @PostMapping("/api/video/{id}/update/name")
    @CrossOrigin
    public ResponseEntity<?> updateVideoName(Principal principal,
                                             @PathVariable(name = "id") Long id,
                                             @RequestBody NameUpdateDto requestDto) {
        Video currentVideo = videoService.findById(id);

        if (currentVideo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();

        try {
            videoService.updateName(currentProfile, currentVideo, requestDto.getName());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>() {{
                put("Error", e.getMessage());
            }});
        }

        return ResponseEntity.ok(VideoDto.fromVideo(currentVideo));
    }

}
