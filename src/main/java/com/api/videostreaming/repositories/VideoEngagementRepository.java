package com.api.videostreaming.repositories;


import com.api.videostreaming.entities.Video;
import com.api.videostreaming.entities.VideoEngagements;
import com.api.videostreaming.enums.EngagementType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoEngagementRepository extends JpaRepository<VideoEngagements, Long> {
    Optional<VideoEngagements> findByVideoAndUserId(Video video, Long userId);
    Optional<VideoEngagements> findByVideoId(Long videoId);
    
}

