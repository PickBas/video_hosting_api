package com.therearenotasksforus.videohostingapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class UpdateUserDto {
    private String firstName;
    private String lastName;
}
