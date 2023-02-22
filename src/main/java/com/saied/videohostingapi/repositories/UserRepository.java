package com.saied.videohostingapi.repositories;

import java.util.Optional;

import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findByProfile(Profile profile);
}
