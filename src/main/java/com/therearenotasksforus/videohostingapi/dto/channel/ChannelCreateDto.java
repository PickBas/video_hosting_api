package com.therearenotasksforus.videohostingapi.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ChannelCreateDto {
    private String name;
    private String info;
}
