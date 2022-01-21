package com.therearenotasksforus.videohostingapi.service;

import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;
import com.therearenotasksforus.videohostingapi.models.marks.Comment;
import com.therearenotasksforus.videohostingapi.models.marks.Dislike;
import com.therearenotasksforus.videohostingapi.models.marks.Like;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    Long uploadVideo(Profile profile, Channel channel, MultipartFile file);
    void updateName(Profile profile, Video video, String name);
    Video findById(Long id);
    Like findLikeById(Long id);
    Dislike findDislikeById(Long id);
    Comment findCommentById(Long id);
    List<Video> getAll();
    List<Like> getAllLikes(Video video);
    List<Dislike> getAllDislikes(Video video);
    List<Comment> getAllComments(Video video);
    void setLike(Profile profile, Video video);
    void setDislike(Profile profile, Video video);
    void saveComment(Profile profile, Video video, String commentBody);
    void delete(Long id);
    void deleteLikes(Profile profile, Video video, Long id);
    void deleteDislikes(Video video, Long id);
    void deleteComments(Video video, Long id);

}
