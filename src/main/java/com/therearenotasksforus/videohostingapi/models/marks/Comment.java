package com.therearenotasksforus.videohostingapi.models.marks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.therearenotasksforus.videohostingapi.models.BaseEntity;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    @ManyToOne
    @JsonIgnore
    private Profile profile;

    @ManyToOne
    @JsonIgnore
    private Video video;

    @Column(name = "time")
    private Timestamp timestamp;

    @Column(name = "comment_body")
    private String commentBody;

    public Comment() {
        profile = null;
        video = null;
        timestamp = new Timestamp(System.currentTimeMillis());
        commentBody = "";
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }
}
