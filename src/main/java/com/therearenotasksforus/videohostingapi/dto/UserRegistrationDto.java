package com.therearenotasksforus.videohostingapi.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String email;
    private String username;
    private String password;
}
