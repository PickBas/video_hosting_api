package com.therearenotasksforus.videohostingapi.security.models;

import com.therearenotasksforus.videohostingapi.models.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "invalid_tokens")
public class InvalidJwts extends BaseEntity {
    @Column(name = "token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
