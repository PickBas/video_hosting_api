package com.saied.videohostingapi.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @Builder
@Table(name = "videos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @EqualsAndHashCode.Include
    private Channel channel;
    @Column(name = "video_name")
    @EqualsAndHashCode.Include
    private String name;
    @CreatedDate
    @Column(name = "created")
    @EqualsAndHashCode.Include
    private Timestamp created;
    @LastModifiedDate
    @Column(name = "updated")
    @EqualsAndHashCode.Include
    private Timestamp updated;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Profile> likes;

}
