package com.therearenotasksforus.videohostingapi.dto.profile;

public class ProfileUpdateDto {
    private String aboutProfileInfo;
    private char gender;
    private String country;
    private String customUrl;
    private boolean privateSublist;

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

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public boolean getPrivateSublist() {
        return privateSublist;
    }

    public void setPrivateSublist(boolean privateSublist) {
        this.privateSublist = privateSublist;
    }

}
