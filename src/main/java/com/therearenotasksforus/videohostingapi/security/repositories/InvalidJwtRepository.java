package com.therearenotasksforus.videohostingapi.security.repositories;

import com.therearenotasksforus.videohostingapi.security.models.InvalidJwts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidJwtRepository extends JpaRepository<InvalidJwts, Long> {
    InvalidJwts findByToken(String token);
}
