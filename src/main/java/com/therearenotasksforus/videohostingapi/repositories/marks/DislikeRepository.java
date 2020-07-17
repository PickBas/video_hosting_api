package com.therearenotasksforus.videohostingapi.repositories.marks;

import com.therearenotasksforus.videohostingapi.models.marks.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {
}
