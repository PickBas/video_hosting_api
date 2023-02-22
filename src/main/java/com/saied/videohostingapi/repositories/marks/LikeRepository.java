package com.saied.videohostingapi.repositories.marks;

import com.saied.videohostingapi.models.marks.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
