package com.api.videostreaming.pojos.responses;

import lombok.*;

@Getter
@Setter
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
