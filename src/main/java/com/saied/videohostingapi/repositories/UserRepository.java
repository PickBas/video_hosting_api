package com.saied.videohostingapi.repositories;

import com.saied.videohostingapi.models.Profile;
import com.saied.videohostingapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
    User findByProfile(Profile profile);
}
