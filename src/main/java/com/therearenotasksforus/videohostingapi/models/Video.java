package com.therearenotasksforus.videohostingapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.therearenotasksforus.videohostingapi.models.marks.Comment;
import com.therearenotasksforus.videohostingapi.models.marks.Dislike;
import com.therearenotasksforus.videohostingapi.models.marks.Like;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "videos")
public class Video extends BaseEntity {
    @ManyToOne
    private Channel channel;

    @Column(name = "time")
    private Timestamp timestamp;

    @Column(name = "video_file_url")
    private String videoFileUrl;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Column(name = "is_available_by_link")
    private boolean isAvailableByLink;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likes;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Dislike> dislikes;

    public Video() {
        channel = null;
        timestamp = new Timestamp(System.currentTimeMillis());
        videoFileUrl = "";
        isPrivate = false;
        isAvailableByLink = false;
        likes = new ArrayList<>();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getVideoFileUrl() {
        return videoFileUrl;
    }

    public void setVideoFileUrl(String videoFileUrl) {
        this.videoFileUrl = videoFileUrl;
    }

    public boolean isIs_private() {
        return isPrivate;
    }

    public void setIs_private(boolean is_private) {
        this.isPrivate = is_private;
    }

    public boolean isIs_available_by_link() {
        return isAvailableByLink;
    }

    public void setIs_available_by_link(boolean is_available_by_link) {
        this.isAvailableByLink = is_available_by_link;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Dislike> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<Dislike> dislikes) {
        this.dislikes = dislikes;
    }

    public void addLike(Like like) {
        likes.add(like);
    }

    public void removeLike(Like like) {
        likes.remove(like);
    }

    public void addDislike(Dislike dislike) {
        dislikes.add(dislike);
    }

    public void removeLike(Dislike dislike) {
        dislikes.remove(dislike);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }
}