package com.diogo.portfolio_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.diogo.portfolio_backend.dto.ProjectRequest;
import com.diogo.portfolio_backend.dto.ProjectResponse;
import com.diogo.portfolio_backend.entity.Project;
import com.diogo.portfolio_backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final Cloudinary cloudinary;

    public ProjectService(ProjectRepository repository,
                          @Value("${cloudinary.cloud-name}") String cloudName,
                          @Value("${cloudinary.api-key}") String apiKey,
                          @Value("${cloudinary.api-secret}") String apiSecret) {

        this.repository = repository;
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    public ProjectResponse create(ProjectRequest request, MultipartFile image) throws IOException {
        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setLink(request.getLink());

        if (image != null && !image.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("folder", "portfolio/uploads/projects"));
            project.setImagePublicId((String) uploadResult.get("public_id")); // save public ID
        }

        repository.save(project);
        return map(project);
    }

    public List<ProjectResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (project.getImagePublicId() != null) {
            try {
                cloudinary.uploader().destroy(project.getImagePublicId(), ObjectUtils.emptyMap());
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete project image from Cloudinary", e);
            }
        }

        repository.deleteById(id);
    }

    public ProjectResponse update(Long id, ProjectRequest request, MultipartFile image) throws IOException {
        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setLink(request.getLink());

        if (image != null && !image.isEmpty()) {
            // Delete old image from Cloudinary
            if (project.getImagePublicId() != null) {
                cloudinary.uploader().destroy(project.getImagePublicId(), ObjectUtils.emptyMap());
            }

            // Upload new image
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("folder", "portfolio/uploads/projects"));
            project.setImagePublicId((String) uploadResult.get("public_id"));
        }

        repository.save(project);
        return map(project);
    }

    private ProjectResponse map(Project project) {
        ProjectResponse dto = new ProjectResponse();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setLink(project.getLink());
        dto.setCreatedAt(project.getCreatedAt());

        if (project.getImagePublicId() != null) {
            // Generate Cloudinary URL for the image
            String imageUrl = cloudinary.url().secure(true).generate(project.getImagePublicId());
            dto.setImageUrl(imageUrl); // store full URL in DTO for frontend
        }

        return dto;
    }
}
