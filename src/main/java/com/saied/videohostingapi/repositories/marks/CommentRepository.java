package com.saied.videohostingapi.repositories.marks;

import com.saied.videohostingapi.models.marks.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
