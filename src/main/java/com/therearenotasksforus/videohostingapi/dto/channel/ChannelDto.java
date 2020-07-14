package com.therearenotasksforus.videohostingapi.dto.channel;

import com.therearenotasksforus.videohostingapi.models.Channel;
import com.therearenotasksforus.videohostingapi.models.Profile;

import java.util.ArrayList;
import java.util.List;

public class ChannelDto {
    private Long id;
    private String name;
    private String info;
    private Profile owner;
    private List<Profile> subscribers;

    public static ChannelDto fromChannel(Channel channel) {
        ChannelDto channelDto = new ChannelDto();

        channelDto.id = channel.getId();
        channelDto.name = channel.getName();
        channelDto.info = channel.getInfo();
        channelDto.owner = channel.getOwner();
        channelDto.subscribers = channel.getSubscribers();

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

}
