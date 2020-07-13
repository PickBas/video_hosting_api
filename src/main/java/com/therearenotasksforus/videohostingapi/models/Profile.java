package com.therearenotasksforus.videohostingapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
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

    @Column(name = "is_private_sublist")
    private boolean isPrivateSublist;

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
        isPrivateSublist = false;
        country = "";
        customUrl = "";
        ownedChannels = null;
        subscriptions = null;
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

    public boolean getIsPrivateSublist() {
        return isPrivateSublist;
    }

    public void setIsPrivateSublist(boolean is_private_sublist) {
        this.isPrivateSublist = is_private_sublist;
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
}
