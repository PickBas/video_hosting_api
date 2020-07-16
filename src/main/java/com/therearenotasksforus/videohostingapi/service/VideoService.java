package com.therearenotasksforus.videohostingapi.service;

import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;
import com.therearenotasksforus.videohostingapi.models.marks.Comment;
import com.therearenotasksforus.videohostingapi.models.marks.Dislike;
import com.therearenotasksforus.videohostingapi.models.marks.Like;

import java.util.List;

public interface VideoService {
    void uploadVideo(Profile profile, Channel channel);

    List<Video> getAll();
    List<Like> getAllLikes(Video video);
    List<Dislike> getAllDislikes(Video video);
    List<Comment> getAllComments(Video video);

    void setLike(Profile profile, Video video);
    void setDislike(Profile profile, Video video);
    void saveComment(Profile profile, Video video, String commentBody);

    void delete(Long id);
    void deleteLikes(Long id);
    void deleteDislikes(Long id);
    void deleteComments(Long id);

}