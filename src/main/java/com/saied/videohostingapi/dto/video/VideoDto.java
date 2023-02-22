package com.saied.videohostingapi.dto.video;

import com.saied.videohostingapi.models.Channel;
import com.saied.videohostingapi.models.Video;
import com.saied.videohostingapi.models.marks.Comment;
import com.saied.videohostingapi.models.marks.Dislike;
import com.saied.videohostingapi.models.marks.Like;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VideoDto {
    private Long id;
    private String name;
    private String videoFileUrl;
    private boolean isPrivate;
    private boolean isAvailableByLink;
    private Channel channel;
    private Timestamp created;
    private Timestamp updated;

    private List<Like> likes;
    private List<Dislike> dislikes;
    private List<Comment> comments;

    public static VideoDto fromVideo(Video video) {
        VideoDto videoDto = new VideoDto();

        videoDto.setId(video.getId());
        videoDto.setName(video.getName());
        videoDto.setVideoFileUrl(video.getVideoFileUrl());
        videoDto.setCreated(video.getCreated());
        videoDto.setUpdated(video.getUpdated());
        videoDto.setPrivate(video.isPrivate());
        videoDto.setAvailableByLink(video.isAvailableByLink());
        videoDto.setLikes(video.getLikes());
        videoDto.setDislikes(video.getDislikes());
        videoDto.setComments(video.getComments());
        videoDto.setChannel(video.getChannel());

        return videoDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoFileUrl() {
        return videoFileUrl;
    }

    public void setVideoFileUrl(String videoFileUrl) {
        this.videoFileUrl = videoFileUrl;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isAvailableByLink() {
        return isAvailableByLink;
    }

    public void setAvailableByLink(boolean availableByLink) {
        isAvailableByLink = availableByLink;
    }

    public Long getChannel() {
        return channel.getId();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<Long> getLikes() {
        List<Long> likeIds = new ArrayList<>();

        for (Like like : likes) {
            likeIds.add(like.getId());
        }

        return likeIds;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Long> getDislikes() {
        List<Long> dislikeIds = new ArrayList<>();

        for (Dislike dislike : dislikes) {
            dislikeIds.add(dislike.getId());
        }

        return dislikeIds;
    }

    public void setDislikes(List<Dislike> dislikes) {
        this.dislikes = dislikes;
    }

    public List<Long> getComments() {
        List<Long> commentIds = new ArrayList<>();

        for (Comment comment : comments) {
            commentIds.add(comment.getId());
        }

        return commentIds;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}
