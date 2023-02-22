package com.saied.videohostingapi.repositories.marks;

import com.saied.videohostingapi.models.marks.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {
}
