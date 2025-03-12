package com.api.videostreaming.controllers;

import com.api.videostreaming.enums.EngagementType;
import com.api.videostreaming.pojos.responses.EngagementResponse;
import com.api.videostreaming.services.EngagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/engagements")
@RequiredArgsConstructor
@Slf4j
public class EngagementController {
    
    private final EngagementService engagementTrackingService;

    @Operation(
            summary = "Track engagement for a video",
            description = "Records impressions or views for a video based on user actions.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PostMapping("/{videoId}")
    public ResponseEntity<Void> trackEngagement(
            @PathVariable Long videoId,
            @RequestParam Long userId,
            @RequestParam EngagementType type) {

        log.info("Tracking engagement: Video ID={}, User ID={}, Type={}", videoId, userId, type);

        engagementTrackingService.trackEngagement(videoId, userId, type);

        log.info("Engagement tracked successfully.");

        // Return HTTP 204 No Content (as the service method is void)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Get engagement stats for a video",
            description = "Retrieves engagement statistics including impressions and views for a given video.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @GetMapping("/{videoId}")
    public ResponseEntity<EngagementResponse> getEngagement(@PathVariable Long videoId) {

        log.info("Fetching engagement stats for Video ID={}", videoId);

        ResponseEntity<EngagementResponse> response = engagementTrackingService.getEngagements(videoId);

        log.info("Returning engagement stats: {}", response.getBody());

        return response;
    }
}
