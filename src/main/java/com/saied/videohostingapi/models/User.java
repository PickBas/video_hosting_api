package com.saied.videohostingapi.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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
@NoArgsConstructor
@Getter @Setter @Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @JoinTable(
        name = "user_profiles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "profile_id", referencedColumnName = "id")}
    )
    private Profile profile;
    @Column(name = "username", unique = true)
    @EqualsAndHashCode.Include
    private String username;
    @Column(name = "first_name")
    @EqualsAndHashCode.Include
    private String firstName;
    @Column(name = "last_name")
    @EqualsAndHashCode.Include
    private String lastName;
    @Column(name = "email", unique = true)
    @EqualsAndHashCode.Include
    private String email;
    @Column(name = "password")
    @EqualsAndHashCode.Include
    private String password;
    @CreatedDate
    @Column(name = "created")
    @EqualsAndHashCode.Include
    private Timestamp created;
    @LastModifiedDate
    @Column(name = "updated")
    @EqualsAndHashCode.Include
    private Timestamp updated;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")
        }
    )
    private List<Role> roles;
}
