package com.therearenotasksforus.videohostingapi.dto.profile;

import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileDto {
    private Long id;
    private User user;
    private String aboutProfileInfo;
    private char gender;
    private String country;
    private String customUrl;
    private boolean isPrivateSublist;
    private List<Channel> ownedChannels;
    private List<Channel> subscriptions;

    public Profile toUser(){
        Profile profile = new Profile();
        profile.setId(id);
        profile.setAboutProfileInfo(aboutProfileInfo);
        profile.setGender(gender);
        profile.setCountry(country);
        profile.setCustomUrl(customUrl);
        profile.setUser(user);
        profile.setOwnedChannels(ownedChannels);
        profile.setSubscriptions(subscriptions);

        return profile;
    }

    public static ProfileDto fromUser(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setAboutProfileInfo(profile.getAboutProfileInfo());
        profileDto.setGender(profile.getGender());
        profileDto.setCountry(profile.getCountry());
        profileDto.setCustomUrl(profile.getCustomUrl());
        profileDto.setUser(profile.getUser());
        profileDto.setOwnedChannels(profile.getOwnedChannels());
        profileDto.setSubscriptions(profile.getSubscriptions());

        return profileDto;
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

    public boolean isPrivateSublist() {
        return isPrivateSublist;
    }

    public void setPrivateSublist(boolean privateSublist) {
        isPrivateSublist = privateSublist;
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

}
