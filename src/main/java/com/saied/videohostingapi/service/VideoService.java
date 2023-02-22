package com.saied.videohostingapi.service;

import com.saied.videohostingapi.models.Channel;
import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.Video;
import com.saied.videohostingapi.models.marks.Comment;
import com.saied.videohostingapi.models.marks.Dislike;
import com.saied.videohostingapi.models.marks.Like;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    Long uploadVideo(Profile profile, Channel channel, MultipartFile file);
    void updateName(Profile profile, Video video, String name);
    Video findById(Long id);
    Like findLikeById(Long id);
    Dislike findDislikeById(Long id);
    Comment findCommentById(Long id);
    List<Video> getVideosPaginated(int pageNumber, int page);
    List<Like> getLikesPaginated(Video video, int pageNumner, int page);
    List<Dislike> getAllDislikes(Video video, int pageNumner, int page);
    List<Comment> getAllComments(Video video, int pageNumner, int page);
    void setLike(Profile profile, Video video);
    void setDislike(Profile profile, Video video);
    void saveComment(Profile profile, Video video, String commentBody);
    void delete(Long id);
    void deleteLikes(Profile profile, Video video, Long id);
    void deleteDislikes(Video video, Long id);
    void deleteComments(Video video, Long id);

}