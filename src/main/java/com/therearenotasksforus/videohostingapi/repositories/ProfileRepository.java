package com.therearenotasksforus.videohostingapi.repositories;

import com.therearenotasksforus.videohostingapi.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
