package com.diogo.portfolio_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CvFile {
    @Id
    private Long id = 1L;

    private String publicId;
    private String url;
    private String contentType;

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
