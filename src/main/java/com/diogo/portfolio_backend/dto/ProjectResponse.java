package com.diogo.portfolio_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String link;
    private LocalDateTime createdAt;
}
