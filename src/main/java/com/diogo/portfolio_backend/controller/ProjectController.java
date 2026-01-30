package com.diogo.portfolio_backend.controller;

import com.diogo.portfolio_backend.dto.ProjectRequest;
import com.diogo.portfolio_backend.dto.ProjectResponse;
import com.diogo.portfolio_backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ProjectController {
    private final ProjectService service;
    public ProjectController(ProjectService service){
        this.service = service;
    }

    @GetMapping("/api/public/projects")
    public List<ProjectResponse> getProjects(){
        return service.findAll();
    }

    @PostMapping(value = "/api/admin/projects", consumes = "multipart/form-data")
    public ResponseEntity<ProjectResponse> createProject(@RequestPart("data") ProjectRequest request, @RequestPart("image")MultipartFile image) throws IOException {
        return ResponseEntity.ok(service.create(request, image));
    }

    @DeleteMapping("/api/admin/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/api/admin/projects/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @RequestPart("data") ProjectRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {

        return ResponseEntity.ok(service.update(id, request, image));
    }

}
