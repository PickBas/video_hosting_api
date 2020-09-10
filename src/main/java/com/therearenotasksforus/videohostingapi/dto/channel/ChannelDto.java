package com.therearenotasksforus.videohostingapi.dto.channel;

import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.Video;

import java.util.ArrayList;
import java.util.List;

public class ChannelDto {
    private Long id;
    private String name;
    private String info;
    private Profile owner;
    private String avatarUrl;
    private List<Profile> subscribers;
    private List<Video> videos;

    public static ChannelDto fromChannel(Channel channel) {
        ChannelDto channelDto = new ChannelDto();

        channelDto.id = channel.getId();
        channelDto.name = channel.getName();
        channelDto.info = channel.getInfo();
        channelDto.owner = channel.getOwner();
        channelDto.subscribers = channel.getSubscribers();
        channelDto.videos = channel.getVideos();
        channelDto.avatarUrl = channel.getAvatarUrl();

        return channelDto;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getOwner() {
        return owner.getId();
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public List<Long> getSubscribers() {
        List<Long> subscribersIds = new ArrayList<>();

        for (Profile subscriber : subscribers) {
            subscribersIds.add(subscriber.getId());
        }

        return subscribersIds;
    }

    public void setSubscribers(List<Profile> subscribers) {
        this.subscribers = subscribers;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
