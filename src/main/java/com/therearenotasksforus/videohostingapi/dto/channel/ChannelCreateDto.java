package com.therearenotasksforus.videohostingapi.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ChannelCreateDto {
    private String name;
    private String info;
}
