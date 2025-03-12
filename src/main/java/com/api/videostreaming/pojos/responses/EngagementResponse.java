package com.api.videostreaming.pojos.responses;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngagementResponse {
    private Long videoId;
    private Long userId;
    private String title;
    private int impressions;
    private int views;
    private String message;
    private boolean success;
}
