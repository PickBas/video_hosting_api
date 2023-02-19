package com.therearenotasksforus.videohostingapi.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class LogoutRequestDto {
    private String logout;
}
