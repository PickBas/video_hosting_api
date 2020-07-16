package com.therearenotasksforus.videohostingapi.repositories.marks;

import com.therearenotasksforus.videohostingapi.models.marks.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
