package com.api.videostreaming.controllers;

import ch.qos.logback.classic.Logger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.videostreaming.pojos.requests.MetadataRequest;
import com.api.videostreaming.pojos.requests.VideoRequest;
import com.api.videostreaming.pojos.responses.MetadataResponse;
import com.api.videostreaming.pojos.responses.PlayVideoResponse;
import com.api.videostreaming.pojos.responses.PublishVideoResponse;
import com.api.videostreaming.pojos.responses.SearchVideoResponse;
import com.api.videostreaming.pojos.responses.SoftDeleteResponse;
import com.api.videostreaming.services.VideoService;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {
    private static final Logger log = (Logger) LoggerFactory.getLogger(VideoController.class);
    private final VideoService videoService;

    @Operation(
            summary = "API to publish video",
            description = "This endpoint allows authenticated users to publish videos.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PostMapping("/publish")
    public ResponseEntity<PublishVideoResponse> publishVideo(@Valid @RequestBody VideoRequest request) {
        log.info("Received request to publish video: {}", request.getTitle());
        ResponseEntity<PublishVideoResponse> response = videoService.publishVideo(request);
        log.info("Response: Status = {}, Video ID = {}, Title = {}", response.getStatusCode(), response.getBody().getVideoId(), response.getBody().getTitle());
        return response;
    }

    @Operation(
            summary = "API to add or edit video metadata",
            description = "This endpoint allows authenticated users to add or update metadata for a video.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PutMapping("/{videoId}/metadata")
    public ResponseEntity<MetadataResponse> addOrEditVideoMetadata(
            @PathVariable Long videoId,
            @Valid @RequestBody MetadataRequest request) {
        
        log.info("Received request to update metadata for video ID: {}", videoId);
        ResponseEntity<MetadataResponse> response = videoService.addOrEditVideoMetadata(videoId, request);
        log.info("Response: Status = {}, Video ID = {}", response.getStatusCode(), videoId);      
        return response;
    }

    @Operation(
            summary = "API to soft delete a video",
            description = "Marks a video as inactive instead of permanently deleting it.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @DeleteMapping("/{videoId}/soft-delete")
    public ResponseEntity<SoftDeleteResponse> softDeleteVideo(@PathVariable Long videoId) {
        
        log.info("Received request to soft delete video ID: {}", videoId);
        ResponseEntity<SoftDeleteResponse> response = videoService.softDeleteVideo(videoId);
        log.info("Response: Status = {}, Video ID = {}", response.getStatusCode(), videoId); 
        return response;
    }

    @Operation(
            summary = "API to play video content",
            description = "Fetches video content URL based on video ID.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @GetMapping("/{videoId}/play")
    public ResponseEntity<PlayVideoResponse> playVideoContent(@PathVariable Long videoId) {
        log.info("Received request to play video for ID: {}", videoId);
        ResponseEntity<PlayVideoResponse> response = videoService.playVideoContent(videoId);
        log.info("Response: Status = {}, Video ID = {}", response.getStatusCode(), videoId);
        return response;
    }

    @Operation(
            summary = "API: to search on metadata (title, director, genre, cast)",
            description = "Search for videos using a search phrase across title, director, genre, and cast.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @GetMapping("/search")
    public ResponseEntity<Page<SearchVideoResponse>> searchVideos(
            @RequestParam String searchPhrase,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        
        log.info("Received search request: phrase='{}', page={}, size={}", searchPhrase, page, size);
        ResponseEntity<Page<SearchVideoResponse>> response = videoService.searchVideos(searchPhrase, page, size);
        log.info("Response: Status = {}, Total Videos Found = {}", response.getStatusCode(), response.getBody().getTotalElements());
        return response;
    }


}
