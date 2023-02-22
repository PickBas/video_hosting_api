package com.saied.videohostingapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UpdatePasswordDto {
    @JsonProperty("old_password")
    private String oldPassword;
    @JsonProperty("updated_password")
    private String newPassword;
}
