package com.therearenotasksforus.videohostingapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.CascadeType;
import java.util.List;

@Entity
@Table(name = "channels")
public class Channel extends BaseEntity {
    @ManyToOne
    @JsonIgnore
    private Profile owner;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Profile> subscribers;

    @Column(name = "name")
    private String name;

    @Column(name = "info")
    private String info;

    // TODO: Add videos and playlists
    // private Video videos;
    // private Playlist playlists;

    public Channel() {
        owner = null;
        subscribers = null;
        name = "";
        info = "";
    }


    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public List<Profile> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Profile> subscribers) {
        this.subscribers = subscribers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
