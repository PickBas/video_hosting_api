package com.therearenotasksforus.videohostingapi.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String firstName;
    private String lastName;

    private final Timestamp dateJoined;

    private String aboutProfileInfo;
    private char gender;
    private String country;

    @Column(unique=true)
    private String custom_url;

    private boolean is_private_sublist;

    public Profile() {
        username = "";
        firstName = "";
        lastName = "";
        dateJoined = new Timestamp(System.currentTimeMillis());
        aboutProfileInfo = "";
        gender = 'M';
        is_private_sublist = false;
        country = "";
        custom_url = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getDateJoined() {
        return dateJoined;
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

    public String getCustom_url() {
        return custom_url;
    }

    public void setCustom_url(String custom_url) {
        this.custom_url = custom_url;
    }

    public boolean isIs_private_sublist() {
        return is_private_sublist;
    }

    public void setIs_private_sublist(boolean is_private_sublist) {
        this.is_private_sublist = is_private_sublist;
    }
}
