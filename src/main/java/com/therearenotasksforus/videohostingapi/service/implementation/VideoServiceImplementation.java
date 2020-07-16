package com.therearenotasksforus.videohostingapi.service.implementation;

import com.therearenotasksforus.videohostingapi.bucket.BucketName;
import com.therearenotasksforus.videohostingapi.filestore.FileStore;
import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;
import com.therearenotasksforus.videohostingapi.models.marks.Comment;
import com.therearenotasksforus.videohostingapi.models.marks.Dislike;
import com.therearenotasksforus.videohostingapi.models.marks.Like;
import com.therearenotasksforus.videohostingapi.repositories.ChannelRepository;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import com.therearenotasksforus.videohostingapi.repositories.VideoRepository;
import com.therearenotasksforus.videohostingapi.repositories.marks.CommentRepository;
import com.therearenotasksforus.videohostingapi.repositories.marks.DislikeRepository;
import com.therearenotasksforus.videohostingapi.repositories.marks.LikeRepository;
import com.therearenotasksforus.videohostingapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class VideoServiceImplementation implements VideoService {

    private final VideoRepository videoRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final CommentRepository commentRepository;

    private final ProfileRepository profileRepository;
    private final ChannelRepository channelRepository;

    private final FileStore fileStore;

    @Autowired
    public VideoServiceImplementation(VideoRepository videoRepository, LikeRepository likeRepository, DislikeRepository dislikeRepository, CommentRepository commentRepository, ProfileRepository profileRepository, ChannelRepository channelRepository, FileStore fileStore) {
        this.videoRepository = videoRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
        this.commentRepository = commentRepository;
        this.profileRepository = profileRepository;
        this.channelRepository = channelRepository;
        this.fileStore = fileStore;
    }

    @Override
    public void uploadVideo(Profile profile, Channel channel, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Failure: cannot upload empty file [ " + file.getSize() + "]");
        }

        String basicUrl = "https://therearenotasksforus-assets.s3.eu-north-1.amazonaws.com/";

        Map<String, String> metadata = new HashMap<>();

        metadata.put("Content-Type", file.getContentType());

        System.out.println(file.getContentType());

        metadata.put("content-length", String.valueOf(file.getSize()));

        String originalFileName = Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", "_");

        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), channel.getId());
        String filename = String.format("%s-%s", UUID.randomUUID(), originalFileName);

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());

            Video video = new Video();
            video.setVideoFileUrl(basicUrl + profile.getId() + "/" + filename);
            videoRepository.save(video);

            channel.addVideo(video);
            channelRepository.save(channel);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Video findVideoById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    @Override
    public List<Like> getAllLikes(Video video) {
        return video.getLikes();
    }

    @Override
    public List<Dislike> getAllDislikes(Video video) {
        return video.getDislikes();
    }

    @Override
    public List<Comment> getAllComments(Video video) {
        return video.getComments();
    }

    public Like isLikeSet(Profile profile, Video video) {
        for (Like like : video.getLikes()) {
            if (like.getOwner() == profile) {
                return like;
            }
        }
        return null;
    }

    public Dislike isDislikeSet(Profile profile, Video video) {
        for (Dislike dislike : video.getDislikes()) {
            if (dislike.getOwner() == profile) {
                return dislike;
            }
        }
        return null;
    }

    public void processLikes(Profile profile, Video video) {
        Like like = new Like();
        like.setOwner(profile);
        like.setVideo(video);
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

        deleteLikes(isSetLike.getId());

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

        deleteDislikes(isSetDislike.getId());

    }

    @Override
    public void saveComment(Profile profile, Video video, String commentBody) {
        Comment comment = new Comment();
        comment.setProfile(profile);
        comment.setCommentBody(commentBody);
        comment.setVideo(video);
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
    public void deleteLikes(Long id) {
        likeRepository.deleteById(id);
    }

    @Override
    public void deleteDislikes(Long id) {
        dislikeRepository.deleteById(id);
    }

    @Override
    public void deleteComments(Long id) {
        commentRepository.deleteById(id);
    }
}
