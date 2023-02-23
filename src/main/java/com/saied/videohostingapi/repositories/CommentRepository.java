package com.saied.videohostingapi.repositories;

import com.saied.videohostingapi.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
