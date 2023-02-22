package com.saied.videohostingapi.service.impl;

import com.saied.videohostingapi.bucket.BucketName;
import com.saied.videohostingapi.filestore.FileStore;
import com.saied.videohostingapi.models.Channel;
import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.Video;
import com.saied.videohostingapi.models.marks.Comment;
import com.saied.videohostingapi.models.marks.Dislike;
import com.saied.videohostingapi.models.marks.Like;
import com.saied.videohostingapi.repositories.ChannelRepository;
import com.saied.videohostingapi.repositories.ProfileRepository;
import com.saied.videohostingapi.repositories.VideoRepository;
import com.saied.videohostingapi.repositories.marks.CommentRepository;
import com.saied.videohostingapi.repositories.marks.DislikeRepository;
import com.saied.videohostingapi.repositories.marks.LikeRepository;
import com.saied.videohostingapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final ChannelRepository channelRepository;
    private final FileStore fileStore;

    @Autowired
    public VideoServiceImpl(
        VideoRepository videoRepository,
        LikeRepository likeRepository,
        DislikeRepository dislikeRepository,
        CommentRepository commentRepository,
        ProfileRepository profileRepository,
        ChannelRepository channelRepository,
        FileStore fileStore
    ) {
        this.videoRepository = videoRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
        this.commentRepository = commentRepository;
        this.profileRepository = profileRepository;
        this.channelRepository = channelRepository;
        this.fileStore = fileStore;
    }

    @Override
    public Long uploadVideo(Profile profile, Channel channel, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Failure: cannot upload empty file [ " + file.getSize() + "]");
        }
        if (!Arrays.asList("video/x-matroska", "video/quicktime", "video/mp4",
                            "video/avi", "video/mpeg").contains(file.getContentType())) {
            throw new IllegalStateException("Failure: the API does not support this file format!");
        }
        String basicUrl =
                "https://"
                + BucketName.BUCKET.getBucketName()
                + ".s3."
                + BucketName.BUCKET.getBucketRegion()
                + ".amazonaws.com/";
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("content-length", String.valueOf(file.getSize()));
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", "_");
        String path = String.format("%s/%s", BucketName.BUCKET.getBucketName(), channel.getId());
        String filename = String.format("%s-%s", UUID.randomUUID(), originalFileName);
        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            Video video = new Video();
            video.setVideoFileUrl(basicUrl + profile.getId() + "/" + filename);
            video.setName(filename);
            video.setChannel(channel);
            video.setUpdated(new Timestamp(System.currentTimeMillis()));
            video.setCreated(new Timestamp(System.currentTimeMillis()));
            videoRepository.save(video);
            channel.addVideo(video);
            channelRepository.save(channel);
            return video.getId();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void updateName(Profile profile, Video video, String name) {
        if (profile != video.getChannel().getOwner()) {
            throw new IllegalStateException("Failure: the user does not have rights for this operation!");
        }
        video.setName(name);
        video.setUpdated(new Timestamp(System.currentTimeMillis()));
        videoRepository.save(video);
    }

    @Override
    public Video findById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }

    @Override
    public Like findLikeById(Long id) {
        return likeRepository.findById(id).orElse(null);
    }

    @Override
    public Dislike findDislikeById(Long id) {
        return dislikeRepository.findById(id).orElse(null);
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }


    public Like isLikeSet(Profile profile, Video video) {
        return video
            .getLikes()
            .stream()
            .filter(o -> o.getOwner().equals(profile))
            .findFirst()
            .orElse(null);
    }

    public Dislike isDislikeSet(Profile profile, Video video) {
        return video
            .getDislikes()
            .stream()
            .filter(o -> o.getOwner().equals(profile))
            .findFirst()
            .orElse(null);
    }

    public void processLikes(Profile profile, Video video) {
        Like like = new Like();
        like.setOwner(profile);
        like.setVideo(video);
        like.setCreated(new Timestamp(System.currentTimeMillis()));
        like.setUpdated(new Timestamp(System.currentTimeMillis()));
        likeRepository.save(like);
        profile.addLike(like);
        profileRepository.save(profile);
        video.addLike(like);
        videoRepository.save(video);
    }

    public void processDislikes(Profile profile, Video video) {
        Dislike dislike = new Dislike();
        dislike.setOwner(profile);
        dislike.setVideo(video);
        dislike.setCreated(new Timestamp(System.currentTimeMillis()));
        dislike.setUpdated(new Timestamp(System.currentTimeMillis()));
        dislikeRepository.save(dislike);
        video.addDislike(dislike);
        videoRepository.save(video);
    }

    @Override
    public void setLike(Profile profile, Video video) {
        Like isSetLike = isLikeSet(profile, video);
        Dislike isSetDislike = isDislikeSet(profile, video);
        if (isSetLike == null && isSetDislike == null) {
            processLikes(profile, video);
            return;
        }
        if (isSetDislike != null) {
            video.removeDislike(isSetDislike);
            processLikes(profile, video);
            return;
        }
        deleteLikes(profile, video, isSetLike.getId());
    }

    @Override
    public void setDislike(Profile profile, Video video) {
        Like isSetLike = isLikeSet(profile, video);
        Dislike isSetDislike = isDislikeSet(profile, video);
        if (isSetLike == null && isSetDislike == null) {
            processDislikes(profile, video);
            return;
        }
        if (isSetLike != null) {
            video.removeLike(isSetLike);
            processDislikes(profile, video);
            return;
        }
        deleteDislikes(video, isSetDislike.getId());
    }

    @Override
    public void saveComment(Profile profile, Video video, String commentBody) {
        Comment comment = new Comment();
        comment.setProfile(profile);
        comment.setCommentBody(commentBody);
        comment.setVideo(video);
        comment.setUpdated(new Timestamp(System.currentTimeMillis()));
        comment.setCreated(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);
        profile.addComment(comment);
        profileRepository.save(profile);
        video.addComment(comment);
        videoRepository.save(video);
    }

    @Override
    public void delete(Long id) {
        videoRepository.deleteById(id);
    }

    @Override
    public void deleteLikes(Profile profile, Video video, Long id) {
        Like like = findLikeById(id);
        profile.removeLike(like);
        profileRepository.save(profile);
        video.removeLike(like);
        videoRepository.save(video);
        likeRepository.delete(like);
    }

    @Override
    public void deleteDislikes(Video video, Long id) {
        Dislike dislike = findDislikeById(id);
        video.removeDislike(dislike);
        videoRepository.save(video);
        dislikeRepository.delete(dislike);
    }

    @Override
    public void deleteComments(Video video, Long id) {
        Comment comment = findCommentById(id);
        video.removeComment(comment);
        videoRepository.save(video);
        comment.getProfile().removeComment(comment);
        profileRepository.save(comment.getProfile());
        commentRepository.deleteById(id);
    }
}
