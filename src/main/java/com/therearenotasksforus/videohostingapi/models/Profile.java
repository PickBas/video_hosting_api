package com.therearenotasksforus.videohostingapi.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToOne;

@Entity
@Table(name = "profiles")
public class Profile  extends BaseEntity {
    @OneToOne(mappedBy = "profile")
    private User user;

    @Column(name = "about_profile_info")
    private String aboutProfileInfo;

    @Column(name = "gender")
    private char gender;

    @Column(name = "country")
    private String country;

    @Column(name = "custom_url")
    private String customUrl;

    @Column(name = "is_private_sublist")
    private boolean isPrivateSublist;

    public Profile() {
//        user = null;
        aboutProfileInfo = "";
        gender = 'M';
        isPrivateSublist = false;
        country = "";
        customUrl = "";
    }

    public String getAboutProfileInfo() {
        return aboutProfileInfo;
    }

    public void setAboutProfileInfo(String aboutProfileInfo) {
        this.aboutProfileInfo = aboutProfileInfo;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getcustomUrl() {
        return customUrl;
    }

    public void setcustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public boolean getIsPrivateSublist() {
        return isPrivateSublist;
    }

    public void setIsPrivateSublist(boolean is_private_sublist) {
        this.isPrivateSublist = is_private_sublist;
    }

    public Long getUser() {
        return user.getId();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
