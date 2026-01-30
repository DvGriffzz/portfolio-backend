package com.diogo.portfolio_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.diogo.portfolio_backend.entity.CvFile;
import com.diogo.portfolio_backend.repository.CvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CvService {

    private static final String CV_FOLDER = "portfolio/uploads/cv";

    private final CvRepository repository;
    private final Cloudinary cloudinary;

    public CvFile upload(MultipartFile file) throws IOException {

        // Delete old CV if exists
        repository.findById(1L).ifPresent(old -> {
            if (old.getPublicId() != null) {
                try {
                    cloudinary.uploader().destroy(
                            old.getPublicId(),
                            ObjectUtils.asMap(
                                    "resource_type", "raw",
                                    "invalidate", true
                            )
                    );
                } catch (IOException ignored) {}
            }
        });

        // Upload new CV as RAW file with custom public_id
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", CV_FOLDER,
                        "resource_type", "raw",
                        "public_id", "cv",  // Just "cv", not the full path
                        "overwrite", true
                )
        );

        CvFile cv = new CvFile();
        cv.setId(1L);
        cv.setPublicId((String) uploadResult.get("public_id"));
        cv.setUrl((String) uploadResult.get("secure_url"));
        cv.setContentType(file.getContentType());

        return repository.save(cv);
    }

    public CvFile getCurrentCv() {
        return repository.findById(1L)
                .orElseThrow(() -> new RuntimeException("CV not found"));
    }

    public Optional<CvFile> getCurrentCvOptional() {
        return repository.findById(1L);
    }

}
