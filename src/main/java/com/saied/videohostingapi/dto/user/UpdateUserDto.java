package com.saied.videohostingapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Data
public class UpdateUserDto {
    private String firstName;
    private String lastName;
}
