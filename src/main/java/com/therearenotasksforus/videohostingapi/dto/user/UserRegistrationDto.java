package com.therearenotasksforus.videohostingapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class UserRegistrationDto {
    private String email;
    private String username;
    private String password;

}
