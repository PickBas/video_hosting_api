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
@Table(name = "dislikes")
public class Dislike extends BaseEntity {
    @ManyToOne
    @JsonIgnore
    private Profile owner;

    @ManyToOne
    @JsonIgnore
    private Video video;

    @Column(name = "time")
    private Timestamp timestamp;

    public Dislike() {
        owner = null;
        video = null;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
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
}
