package com.therearenotasksforus.videohostingapi.repositories;

import com.therearenotasksforus.videohostingapi.models.Profile;
import com.therearenotasksforus.videohostingapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByCustomUrl(String customUrl);
    Profile findByUser(User user);
}
