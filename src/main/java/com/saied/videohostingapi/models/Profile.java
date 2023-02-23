package com.saied.videohostingapi.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saied.videohostingapi.bucket.BucketName;
import com.saied.videohostingapi.exceptions.channel.UserIsAlreadySubscribedException;
import com.saied.videohostingapi.exceptions.channel.UserWasNotSubscribedException;
import com.saied.videohostingapi.exceptions.video.CommentWasNotLeftByRequiredUserException;
import com.saied.videohostingapi.exceptions.video.LikeWasAlreadySetException;
import com.saied.videohostingapi.exceptions.video.LikeWasNotSetByRequiredUserException;

import java.sql.Timestamp;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Profile {

    public static final String DEFAULT_AVATAR_URL = "https://"
        + BucketName.BUCKET.getBucketName()
        + ".s3."
        + BucketName.BUCKET.getBucketRegion()
        + ".amazonaws.com/default/unknown_profile.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    private User user;
    @Column(name = "country")
    @EqualsAndHashCode.Include
    private String country;
    @Column(name = "avatar_url")
    @EqualsAndHashCode.Include
    private String avatarUrl;
    @CreatedDate
    @Column(name = "created")
    @EqualsAndHashCode.Include
    private Timestamp created;
    @LastModifiedDate
    @Column(name = "updated")
    @EqualsAndHashCode.Include
    private Timestamp updated;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "profile_liked_video",
        joinColumns = {
            @JoinColumn(name = "profile_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "video_id", referencedColumnName = "id")
        }
    )
    private Set<Video> likedVideos;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
        joinColumns = {
            @JoinColumn(name = "profile_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "comment_id", referencedColumnName = "id")
        }
    )
    private Set<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(
        name = "profile_own_channel",
        joinColumns = {
            @JoinColumn(name = "profile_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "channel_id", referencedColumnName = "id")
        }
    )
    private Set<Channel> ownedChannels;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(
        name = "profile_sub_channel",
        joinColumns = {
            @JoinColumn(name = "profile_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "channel_id", referencedColumnName = "id")
        }
    )
    private Set<Channel> subscriptions;

    /**
     * Adding newly created channel to ownedChannels Set
     * @param channel Channel entity
     */
    public void addOwnedChannel(Channel channel) {
        this.ownedChannels.add(channel);
    }

    /**
     * Adding newly created like to `likes` Set
     * @param video Liked video entity
     * @throws LikeWasAlreadySetException if like was already set
     */
    public void addLike(Video video) throws LikeWasAlreadySetException {
        if (this.likedVideos.contains(video)) {
            throw new LikeWasAlreadySetException(
                String.format(
                    "Like on this video was already set by profile with id: %s",
                    this.getId()
                )
            );
        }
        this.likedVideos.add(video);
    }

    /**
     * Adding newly created like to `comments` Set
     * @param comment Comment entity
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    /**
     * Adding a channel to `subscriptions` Set
     * @param channel Channel entity
     * @throws UserIsAlreadySubscribedException if user is already subscribed
     */
    public void addSub(Channel channel) throws UserIsAlreadySubscribedException {
        if (this.subscriptions.contains(channel)) {
            throw new UserIsAlreadySubscribedException(
                String.format(
                    "Profile with id: %s; was already subscribed to the channel with id: %s",
                    this.getId(),
                    channel.getId()
                )
            );
        }
        this.subscriptions.add(channel);
    }

    /**
     * Removing video from liked set
     * @param video Video entity
     * @throws LikeWasNotSetByRequiredUserException if like was not set
     */
    public void removeLike(Video video) throws LikeWasNotSetByRequiredUserException {
        if (!this.likedVideos.contains(video)) {
            throw new LikeWasNotSetByRequiredUserException(
                String.format(
                    "Profile with id: %s; did not set like on video with id: %s",
                    this.getId(),
                    video.getId()
                )
            );
        }
        this.likedVideos.remove(video);
    }

    /**
     * Removing the comment from profile's comment Set
     * @param comment Comment entity
     * @throws CommentWasNotLeftByRequiredUserException if the comment was not left by current user
     */
    public void removeComment(Comment comment) throws CommentWasNotLeftByRequiredUserException {
        if (!this.comments.contains(comment)) {
            throw new CommentWasNotLeftByRequiredUserException(
                String.format(
                    "Profile with id: %s; leave comment with id: %s",
                    this.getId(),
                    comment.getId()
                )
            );
        }
        this.comments.remove(comment);
    }

    /**
     * Removing the channel from sublist
     * @param channel Channel entity
     * @throws UserWasNotSubscribedException if user was not subscribed
     */
    public void removeSub(Channel channel) throws UserWasNotSubscribedException {
        if (!this.subscriptions.contains(channel)) {
            throw new UserWasNotSubscribedException(
                String.format(
                    "Profile with id: %s; was not subscribed to the channel with id: %s",
                    this.getId(),
                    channel.getId()
                )
            );
        }
        this.subscriptions.remove(channel);
    }
}
