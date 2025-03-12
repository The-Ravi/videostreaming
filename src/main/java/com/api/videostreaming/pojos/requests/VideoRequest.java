package com.api.videostreaming.pojos.requests;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoRequest {
    @NotBlank
    private String title;
    private String synopsis;
    private String director;
    private List<String> cast;
    private int yearOfRelease;
    private String genre;
    private int runningTime;
}

