package com.therearenotasksforus.videohostingapi.repositories.marks;

import com.therearenotasksforus.videohostingapi.models.marks.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
