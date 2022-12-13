package com.therearenotasksforus.videohostingapi.controllers;

import com.therearenotasksforus.videohostingapi.dto.video.CommentDto;
import com.therearenotasksforus.videohostingapi.dto.video.NameUpdateDto;
import com.therearenotasksforus.videohostingapi.dto.video.VideoDto;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;
import com.therearenotasksforus.videohostingapi.models.marks.Comment;
import com.therearenotasksforus.videohostingapi.service.ChannelService;
import com.therearenotasksforus.videohostingapi.service.ProfileService;
import com.therearenotasksforus.videohostingapi.service.UserService;
import com.therearenotasksforus.videohostingapi.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor @Slf4j
public class VideoController {
    private final VideoService videoService;
    private final ChannelService channelService;
    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping("/api/videos")
    @CrossOrigin
    public List<VideoDto> getAllVideos() {
        List<Video> videos = videoService.getAll();
        List<VideoDto> videoDtos = new ArrayList<>();
        for (Video video : videos) {
            videoDtos.add(VideoDto.fromVideo(video));
        }
        log.info("Loaded all videos. HttpStatus: {}", HttpStatus.OK);
        return videoDtos;
    }

    @GetMapping("/api/video/{id}")
    @CrossOrigin
    public ResponseEntity<VideoDto> getVideo(@PathVariable(name = "id") Long id) {
        Video video = videoService.findById(id);
        if (video == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        log.info("Loaded video with id {}. HttpStatus: {}", id, HttpStatus.OK);
        return ResponseEntity.ok(VideoDto.fromVideo(video));
    }

    @PostMapping("/api/video/{id}/like")
    @CrossOrigin
    public ResponseEntity<VideoDto> setLike(Principal principal, @PathVariable(name = "id") Long id) {
        Video currentVideo = videoService.findById(id);
        if (currentVideo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        videoService.setLike(currentProfile, currentVideo);
        log.info("Set like on video with id {} by user {}. HttpStatus: {}",
                id, currentProfile.getUser().getUsername(), HttpStatus.OK);
        return ResponseEntity.ok(VideoDto.fromVideo(videoService.findById(currentVideo.getId())));
    }

    @PostMapping("/api/video/{id}/dislike")
    @CrossOrigin
    public ResponseEntity<VideoDto> setDislike(Principal principal, @PathVariable(name = "id") Long id) {
        Video currentVideo = videoService.findById(id);
        if (currentVideo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        videoService.setDislike(currentProfile, currentVideo);
        log.info("Set dislike on video with id {} by user {}. HttpStatus: {}",
                id, currentProfile.getUser().getUsername(), HttpStatus.OK);
        return ResponseEntity.ok(VideoDto.fromVideo(videoService.findById(currentVideo.getId())));
    }

    @PostMapping("/api/video/{id}/comment")
    @CrossOrigin
    public ResponseEntity<VideoDto> comment(Principal principal,
                                            @PathVariable(name = "id") Long id,
                                            @RequestBody CommentDto requestDto) {
        Video currentVideo = videoService.findById(id);
        if (currentVideo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        videoService.saveComment(currentProfile, currentVideo, requestDto.getCommentBody());
        log.info("Commented video with id {} by user {}. HttpStatus: {}",
                id, currentProfile.getUser().getUsername(), HttpStatus.OK);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(VideoDto.fromVideo(videoService.findById(id)));
    }

    @DeleteMapping("/api/video/{video_id}/comment/{comment_id}")
    @CrossOrigin
    public ResponseEntity<?> deleteCommentById(Principal principal,
                                                      @PathVariable(name = "video_id") Long videoId,
                                                      @PathVariable(name = "comment_id") Long commentId) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Video currentVideo = videoService.findById(videoId);
        Comment comment = videoService.findCommentById(commentId);
        if (currentVideo == null || comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (currentProfile != comment.getProfile()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        videoService.deleteComments(currentVideo, commentId);
        log.info("Comment with id {} was deleted from video with id {}. HttpStatus: {}",
                commentId, videoId, HttpStatus.OK);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/video/{id}/get/comments")
    @CrossOrigin
    public ResponseEntity<List<Comment>> getComments(@PathVariable(name = "id") Long id) {
        Video currentVideo = videoService.findById(id);
        if (currentVideo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Comment> comments = currentVideo.getComments();
        log.info("Loaded comments of video with id {}. HttpStatus: {}", id, HttpStatus.OK);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/api/channel/{id}/videos")
    @CrossOrigin
    public ResponseEntity<List<VideoDto>> getAllChannelVideos(@PathVariable(name = "id") Long id) {
        Channel channel = channelService.findById(id);
        if (channel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ArrayList<VideoDto> videoDtos = new ArrayList<>();
        for (Video video : channel.getVideos()) {
            videoDtos.add(VideoDto.fromVideo(video));
        }
        log.info("Loaded all videos of channel with id {}. HttpStatus: {}", id, HttpStatus.OK);
        return ResponseEntity.ok(videoDtos);
    }

    @GetMapping("/api/profile/{id}/likedvideos")
    @CrossOrigin
    public ResponseEntity<List<VideoDto>> getAllLikedVideos(@PathVariable(name = "id") Long id) {
        Profile profile = profileService.findById(id);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ArrayList<VideoDto> dtos = new ArrayList<>();
        for (Video video : profile.getLikedVideos()) {
            dtos.add(VideoDto.fromVideo(video));
        }
        log.info("Loaded all liked videos of user {}. HttpStatus: {}",
                profile.getUser().getUsername(), HttpStatus.OK);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping(
            path = "/api/channel/{id}/upload/video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    public ResponseEntity<?> uploadVideo(Principal principal,
                                         @PathVariable(name = "id") Long id,
                                         @RequestParam("file") MultipartFile file) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Channel currentChannel = channelService.findById(id);
        Long newVideoId;
        if (currentProfile != currentChannel.getOwner()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error_message", "This profile is not the channel owner!"));
        }
        try {
            newVideoId = videoService.uploadVideo(currentProfile, currentChannel, file);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error_message", e.getMessage()));
        }
        log.info("Uploaded video on channel with id {}. HttpStatus: {}", id, HttpStatus.OK);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(VideoDto.fromVideo(videoService.findById(newVideoId)));
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error_message", e.getMessage()));
        }
        log.info("Updated title of video with id {}. HttpStatus: {}", id, HttpStatus.OK);
        return ResponseEntity.ok(VideoDto.fromVideo(currentVideo));
    }

    @DeleteMapping("/api/video/{id}")
    @CrossOrigin
    public ResponseEntity<?> deleteVideo(Principal principal,
                                      @PathVariable(name = "id") Long id) {
        Profile currentProfile = userService.findByUsername(principal.getName()).getProfile();
        Video currentVideo = videoService.findById(id);
        if (currentVideo == null) {
            return ResponseEntity.notFound().build();
        }
        if (currentProfile != currentVideo.getChannel().getOwner()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        videoService.delete(id);
        log.info("Deleted video with id {}. HttpStatus: {}", id, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
