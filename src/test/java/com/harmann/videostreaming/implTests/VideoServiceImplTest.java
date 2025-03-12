package com.harmann.videostreaming.implTests;

import com.api.videostreaming.entities.Video;
import com.api.videostreaming.exceptions.InternalServerErrorException;
import com.api.videostreaming.exceptions.ResourceNotFoundException;
import com.api.videostreaming.pojos.requests.MetadataRequest;
import com.api.videostreaming.pojos.requests.VideoRequest;
import com.api.videostreaming.pojos.responses.*;
import com.api.videostreaming.repositories.VideoRepository;
import com.api.videostreaming.serviceImpls.VideoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoServiceImplTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoServiceImpl videoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test: Publish a Video Successfully
     */
    @Test
    void testPublishVideoSuccess() {
        // Arrange
        MetadataRequest metadata = MetadataRequest.builder()
                .genre("Action")
                .yearOfRelease(2023)
                .runningTime(120)
                .build();

        VideoRequest request = new VideoRequest("New Video", "Director1", List.of("Actor1"), metadata);

        when(videoRepository.existsByTitleIgnoreCase(request.getTitle())).thenReturn(false);
        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<PublishVideoResponse> response = videoService.publishVideo(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Video", response.getBody().getTitle());
    }

    /**
     * Test: Publish Video that Already Exists
     */
    @Test
    void testPublishVideo_AlreadyExists() {
        // Arrange
        MetadataRequest metadata = MetadataRequest.builder()
                .genre("Action")
                .yearOfRelease(2023)
                .runningTime(120)
                .build();

        VideoRequest request = new VideoRequest("Existing Video", "Director1", List.of("Actor1"), metadata);

        when(videoRepository.existsByTitleIgnoreCase(request.getTitle())).thenReturn(true);

        // Act
        ResponseEntity<PublishVideoResponse> response = videoService.publishVideo(request);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Test: Publish Video Exception Handling
     */
    @Test
    void testPublishVideo_Exception() {
        // Arrange
        MetadataRequest metadata = MetadataRequest.builder()
                .genre("Action")
                .yearOfRelease(2023)
                .runningTime(120)
                .build();

        VideoRequest request = new VideoRequest("New Video", "Director1", List.of("Actor1"), metadata);

        when(videoRepository.save(any(Video.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(InternalServerErrorException.class, () -> videoService.publishVideo(request));
    }

    /**
     * Test: Load Video Content Successfully
     */
    @Test
    void testLoadVideoContent_Success() {
        // Arrange
        Video video = Video.builder()
                .id(1L)
                .title("Test Video")
                .director("Director")
                .cast(List.of("Actor1"))
                .fileUrl("http://test-video.mp4")
                .isActive(true)
                .build();

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        // Act
        ResponseEntity<LoadVideoResponse> response = videoService.loadVideoContent(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("http://test-video.mp4", response.getBody().getFileUrl());
    }

    /**
     * Test: Play Video Successfully
     */
    @Test
    void testPlayVideoContent_Success() {
        // Arrange
        Video video = Video.builder()
                .id(1L)
                .title("Test Video")
                .director("Director")
                .cast(List.of("Actor1"))
                .fileUrl("http://test-video.mp4")
                .isActive(true)
                .build();

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        // Act
        ResponseEntity<PlayVideoResponse> response = videoService.playVideoContent(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("http://test-video.mp4", response.getBody().getFileUrl());
    }

    /**
     * Test: Search Videos
     */
    @Test
    void testSearchVideos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);
        Video video = Video.builder()
                .id(1L)
                .title("Action Movie")
                .director("Director")
                .cast(List.of("Actor1"))
                .isActive(true)
                .build();

        Page<Video> videoPage = new PageImpl<>(List.of(video));
        when(videoRepository.searchVideos("Action", pageable)).thenReturn(videoPage);

        // Act
        ResponseEntity<Page<SearchVideoResponse>> response = videoService.searchVideos("Action", 0, 5);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }
}
