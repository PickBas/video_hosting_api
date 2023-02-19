package com.therearenotasksforus.videohostingapi.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter @Getter
public class ChannelUpdateDto {
    private String name;
    private String info;
}
