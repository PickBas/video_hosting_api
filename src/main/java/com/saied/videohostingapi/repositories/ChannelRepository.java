package com.saied.videohostingapi.repositories;

import com.saied.videohostingapi.models.Channel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Page<Channel> findAllByOwnerId(@Param("ownerId") Long ownerId, Pageable pageRequest);
}
