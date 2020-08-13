package com.therearenotasksforus.videohostingapi.dto.profile;

import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import com.therearenotasksforus.videohostingapi.models.marks.Like;

import java.util.ArrayList;
import java.util.List;

public class ProfileDto {
    private Long id;
    private User user;
    private String aboutProfileInfo;
    private char gender;
    private String country;
    private String customUrl;
    private String avatarUrl;
    private boolean privateSublist;
    private List<Like> likes;
    private List<Channel> ownedChannels;
    private List<Channel> subscriptions;

    public Profile toProfile(){
        Profile profile = new Profile();

        profile.setId(id);
        profile.setAboutProfileInfo(aboutProfileInfo);
        profile.setGender(gender);
        profile.setCountry(country);
        profile.setCustomUrl(customUrl);
        profile.setAvatarUrl(avatarUrl);
        profile.setLikes(likes);
        profile.setUser(user);
        profile.setOwnedChannels(ownedChannels);
        profile.setSubscriptions(subscriptions);
        profile.setPrivateSublist(privateSublist);

        return profile;
    }

    public static ProfileDto fromProfile(Profile profile) {
        ProfileDto profileDto = new ProfileDto();

        profileDto.setId(profile.getId());
        profileDto.setAboutProfileInfo(profile.getAboutProfileInfo());
        profileDto.setGender(profile.getGender());
        profileDto.setCountry(profile.getCountry());
        profileDto.setCustomUrl(profile.getCustomUrl());
        profileDto.setUser(profile.getUser());
        profileDto.setLikes(profile.getLikes());
        profileDto.setAvatarUrl(profile.getAvatarUrl());
        profileDto.setOwnedChannels(profile.getOwnedChannels());
        profileDto.setSubscriptions(profile.getSubscriptions());
        profileDto.setPrivateSublist(profile.getPrivateSublist());

        return profileDto;
    }

    public Long getId() {
        return id;
    }

    public Long getUser() {
        return user.getId();
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOwnedChannels(List<Channel> ownedChannels) {
        this.ownedChannels = ownedChannels;
    }

    public void setSubscriptions(List<Channel> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Long> getOwnedChannels() {
        List<Long> ownedChannelsIds = new ArrayList<>();

        for (Channel channel : ownedChannels) {
            ownedChannelsIds.add(channel.getId());
        }

        return ownedChannelsIds;
    }

    public List<Long> getSubscriptions() {
        List<Long> subscriptionsIds = new ArrayList<>();

        for (Channel channel : subscriptions) {
            subscriptionsIds.add(channel.getId());
        }

        return subscriptionsIds;
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

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}
