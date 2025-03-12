package com.harmann.videostreaming.implTests;


import com.api.videostreaming.entities.Video;
import com.api.videostreaming.entities.VideoEngagements;
import com.api.videostreaming.enums.EngagementType;
import com.api.videostreaming.exceptions.ResourceNotFoundException;
import com.api.videostreaming.pojos.responses.EngagementResponse;
import com.api.videostreaming.repositories.VideoEngagementRepository;
import com.api.videostreaming.repositories.VideoRepository;
import com.api.videostreaming.serviceImpls.EngagementServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EngagementServiceImplTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoEngagementRepository engagementRepository;

    @InjectMocks
    private EngagementServiceImpl engagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(engagementService, "useKafka", false); // Simulate application.properties
    }

    /**
     * Test: Successfully tracking an impression engagement
     */
    @Test
    void testTrackEngagement_Impression_Success() {
        // Arrange
        Long videoId = 1L, userId = 101L;
        Video video = Video.builder().id(videoId).title("Sample Video").build();
        VideoEngagements engagement = VideoEngagements.builder()
                .video(video)
                .userId(userId)
                .impressions(2)
                .views(0)
                .build();

        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(engagementRepository.findByVideoAndUserId(video, userId)).thenReturn(Optional.of(engagement));

        // Act
        engagementService.trackEngagement(videoId, userId, EngagementType.IMPRESSION);

        // Assert
        assertEquals(3, engagement.getImpressions());
        verify(engagementRepository, times(1)).save(engagement);
    }

    /**
     * Test: Successfully tracking a view engagement
     */
    @Test
    void testTrackEngagement_View_Success() {
        // Arrange
        Long videoId = 2L, userId = 202L;
        Video video = Video.builder().id(videoId).title("Another Video").build();
        VideoEngagements engagement = VideoEngagements.builder()
                .video(video)
                .userId(userId)
                .impressions(5)
                .views(3)
                .build();

        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(engagementRepository.findByVideoAndUserId(video, userId)).thenReturn(Optional.of(engagement));

        // Act
        engagementService.trackEngagement(videoId, userId, EngagementType.VIEW);

        // Assert
        assertEquals(4, engagement.getViews());
        verify(engagementRepository, times(1)).save(engagement);
    }

    /**
     * Test: Create new engagement record if it doesn't exist
     */
    @Test
    void testTrackEngagement_NewRecord() {
        // Arrange
        Long videoId = 3L, userId = 303L;
        Video video = Video.builder().id(videoId).title("New Video").build();

        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(engagementRepository.findByVideoAndUserId(video, userId)).thenReturn(Optional.empty());

        // Act
        engagementService.trackEngagement(videoId, userId, EngagementType.VIEW);

        // Assert
        verify(engagementRepository, times(1)).save(any(VideoEngagements.class));
    }

    /**
     * Test: Track engagement with non-existent video (should throw exception)
     */
    @Test
    void testTrackEngagement_VideoNotFound() {
        // Arrange
        Long videoId = 404L, userId = 505L;

        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> engagementService.trackEngagement(videoId, userId, EngagementType.IMPRESSION));
    }

    /**
     * Test: Get engagement statistics successfully
     */
    @Test
    void testGetEngagements_Success() {
        // Arrange
        Long videoId = 1L;
        Video video = Video.builder().id(videoId).title("Sample Video").build();

        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(engagementRepository.countByVideoAndType(video, EngagementType.IMPRESSION)).thenReturn(10);
        when(engagementRepository.countByVideoAndType(video, EngagementType.VIEW)).thenReturn(5);

        // Act
        ResponseEntity<EngagementResponse> response = engagementService.getEngagements(videoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getImpressions());
        assertEquals(5, response.getBody().getViews());
    }

    /**
     * Test: Get engagement statistics for non-existent video (should throw exception)
     */
    @Test
    void testGetEngagements_VideoNotFound() {
        // Arrange
        Long videoId = 999L;

        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> engagementService.getEngagements(videoId));
    }
}
