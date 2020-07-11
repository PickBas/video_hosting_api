package com.therearenotasksforus.videohostingapi.repositories;

import com.therearenotasksforus.videohostingapi.models.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
