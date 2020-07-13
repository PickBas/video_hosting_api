package com.therearenotasksforus.videohostingapi.repositories;

import com.therearenotasksforus.videohostingapi.models.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
}
