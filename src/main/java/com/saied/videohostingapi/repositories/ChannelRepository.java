package com.saied.videohostingapi.repositories;

import com.saied.videohostingapi.models.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
}
