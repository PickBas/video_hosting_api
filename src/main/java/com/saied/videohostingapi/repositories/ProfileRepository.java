package com.saied.videohostingapi.repositories;

import java.util.Optional;

import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByCustomUrl(String customUrl);
    Optional<Profile> findByUser(User user);
}
