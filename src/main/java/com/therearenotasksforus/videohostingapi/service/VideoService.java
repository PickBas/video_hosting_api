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
    List<Like> getAllLikes();
    List<Dislike> getAllDislikes();
    List<Comment> getAllComments();

    void setLike(Profile profile, Video video);
    void setDislike(Profile profile, Video video);
    void saveComment(Profile profile, Video video);

    void delete();
    void deleteLikes();
    void deleteDislikes();
    void deleteComments();

}
