package com.api.videostreaming.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.videostreaming.entities.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByIsActiveTrue();
    List<Video> findByDirectorContainingIgnoreCase(String director);
}

