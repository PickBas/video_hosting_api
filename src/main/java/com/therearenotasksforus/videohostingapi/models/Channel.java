package com.therearenotasksforus.videohostingapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Video> videos;

    public Channel() {
        owner = null;
        subscribers = new ArrayList<>();
        name = "";
        info = "";
        videos = new ArrayList<>();
        avatarUrl = "https://therearenotasksforus-assets.s3.eu-north-1.amazonaws.com/default/profileavatars/0.jpg";
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

    public void addSubscriber(Profile profile) {
        this.subscribers.add(profile);
    }

    public void removeSubscriber(Profile profile) {
        this.subscribers.remove(profile);
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    public void removeVideo(Video video) {
        videos.remove(video);
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
