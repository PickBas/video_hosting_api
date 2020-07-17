package com.therearenotasksforus.videohostingapi.models.marks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.therearenotasksforus.videohostingapi.models.BaseEntity;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dislikes")
public class Dislike extends BaseEntity {
    @ManyToOne
    @JsonIgnore
    private Profile owner;

    @ManyToOne
    @JsonIgnore
    private Video video;

    public Dislike() {
        owner = null;
        video = null;
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


}
