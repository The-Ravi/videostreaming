package com.api.videostreaming.pojos.requests;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoRequest {
    
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Director name is required")
    private String director;

    @NotNull(message = "Cast list cannot be null")
    private List<String> cast;

    @Valid
    @NotNull(message = "Metadata is required")
    private MetadataRequest metadata;
}

