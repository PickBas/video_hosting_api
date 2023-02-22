package com.saied.videohostingapi.models.marks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.Video;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private Profile profile;
    @ManyToOne
    @JsonIgnore
    private Video video;
    @Column(name = "comment_body")
    private String commentBody;
    @CreatedDate
    @Column(name = "created")
    private Timestamp created;
    @LastModifiedDate
    @Column(name = "updated")
    private Timestamp updated;
}
