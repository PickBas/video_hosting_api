package com.saied.videohostingapi.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saied.videohostingapi.bucket.BucketName;
import com.saied.videohostingapi.models.marks.Comment;
import com.saied.videohostingapi.models.marks.Like;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    public static final String DEFAULT_AVATAR_URL = "https://"
        + BucketName.BUCKET.getBucketName()
        + ".s3."
        + BucketName.BUCKET.getBucketRegion()
        + ".amazonaws.com/default/unknown_profile.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private User user;
    @Column(name = "country")
    private String country;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @CreatedDate
    @Column(name = "created")
    private Timestamp created;
    @LastModifiedDate
    @Column(name = "updated")
    private Timestamp updated;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Like> likes;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Channel> ownedChannels;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Channel> subscriptions;

    public void addOwnedChannel(Channel channel) {
        this.ownedChannels.add(channel);
    }
}
