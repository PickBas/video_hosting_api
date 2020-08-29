package com.therearenotasksforus.videohostingapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.therearenotasksforus.videohostingapi.models.marks.Comment;
import com.therearenotasksforus.videohostingapi.models.marks.Like;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile  extends BaseEntity {
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private User user;

    @Column(name = "about_profile_info")
    private String aboutProfileInfo;

    @Column(name = "gender")
    private char gender;

    @Column(name = "country")
    private String country;

    @Column(name = "custom_url")
    private String customUrl;

    @Column(name = "private_sublist")
    private boolean privateSublist;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Like> likes;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Channel> ownedChannels;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Channel> subscriptions;

    public Profile() {
        user = null;
        aboutProfileInfo = "";
        gender = 'M';
        privateSublist = false;
        country = "";
        customUrl = "";
        ownedChannels = new ArrayList<>();
        subscriptions = new ArrayList<>();
        likes = new ArrayList<>();
        avatarUrl = "https://therearenotasksforus-assets.s3.eu-north-1.amazonaws.com/default/profileavatars/0.jpg";
    }

    public String getAboutProfileInfo() {
        return aboutProfileInfo;
    }

    public void setAboutProfileInfo(String aboutProfileInfo) {
        this.aboutProfileInfo = aboutProfileInfo;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public boolean getPrivateSublist() {
        return privateSublist;
    }

    public void setPrivateSublist(boolean privateSublist) {
        this.privateSublist = privateSublist;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Channel> getOwnedChannels() {
        return ownedChannels;
    }

    public void setOwnedChannels(List<Channel> ownedChannels) {
        this.ownedChannels = ownedChannels;
    }

    public List<Channel> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Channel> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void addOwnedChannel(Channel channel) {
        this.ownedChannels.add(channel);
    }

    public void addSubscription(Channel channel) {
        this.subscriptions.add(channel);
    }

    public void removeSubscription(Channel channel) {
        this.subscriptions.remove(channel);
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void addLike(Like like) {
        likes.add(like);
    }

    public void removeLike(Like like) {
        likes.remove(like);
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

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public List<Video> getLikedVideos() {
        List<Video> likedVideos = new ArrayList<>();

        for (Like like : likes) {
            likedVideos.add(like.getVideo());
        }

        return likedVideos;
    }
}
