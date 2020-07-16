package com.therearenotasksforus.videohostingapi.service.implementation;

import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;
import com.therearenotasksforus.videohostingapi.models.marks.Comment;
import com.therearenotasksforus.videohostingapi.models.marks.Dislike;
import com.therearenotasksforus.videohostingapi.models.marks.Like;
import com.therearenotasksforus.videohostingapi.repositories.ProfileRepository;
import com.therearenotasksforus.videohostingapi.repositories.VideoRepository;
import com.therearenotasksforus.videohostingapi.repositories.marks.CommentRepository;
import com.therearenotasksforus.videohostingapi.repositories.marks.DislikeRepository;
import com.therearenotasksforus.videohostingapi.repositories.marks.LikeRepository;
import com.therearenotasksforus.videohostingapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImplementation implements VideoService {

    private final VideoRepository videoRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final CommentRepository commentRepository;

    private final ProfileRepository profileRepository;

    @Autowired
    public VideoServiceImplementation(VideoRepository videoRepository, LikeRepository likeRepository, DislikeRepository dislikeRepository, CommentRepository commentRepository, ProfileRepository profileRepository) {
        this.videoRepository = videoRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
        this.commentRepository = commentRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public void uploadVideo(Profile profile, Channel channel) {

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

    @Override
    public void setLike(Profile profile, Video video) {
        Like like = new Like();
        like.setOwner(profile);
        like.setVideo(video);
        likeRepository.save(like);

        profile.addLike(like);
        profileRepository.save(profile);

        video.addLike(like);
        videoRepository.save(video);

    }

    @Override
    public void setDislike(Profile profile, Video video) {
        Dislike dislike = new Dislike();
        dislike.setOwner(profile);
        dislike.setVideo(video);
        dislikeRepository.save(dislike);

        video.addDislike(dislike);
        videoRepository.save(video);
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
