package com.api.videostreaming.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String synopsis;
    private String director;
    
    @ElementCollection
    private List<String> cast;
    
    private int yearOfRelease;
    private String genre;
    private int runningTime;
    
    @Column(columnDefinition = "boolean default true")
    private boolean isActive;
}
