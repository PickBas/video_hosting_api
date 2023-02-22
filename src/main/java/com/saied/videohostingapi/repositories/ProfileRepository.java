package com.saied.videohostingapi.repositories;

import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByCustomUrl(String customUrl);
    Profile findByUser(User user);
}
