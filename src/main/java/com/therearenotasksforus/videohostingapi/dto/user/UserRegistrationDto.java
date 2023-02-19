package com.therearenotasksforus.videohostingapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
public class UserRegistrationDto {
    private String email;
    private String username;
    private String password;

}
