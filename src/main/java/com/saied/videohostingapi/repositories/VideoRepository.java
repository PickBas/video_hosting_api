package com.saied.videohostingapi.repositories;

import com.saied.videohostingapi.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findByVideoFileUrl(String fileUrl);
}
