package com.diogo.portfolio_backend.controller;

import com.diogo.portfolio_backend.entity.CvFile;
import com.diogo.portfolio_backend.service.CvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@RestController
@RequiredArgsConstructor
public class CvController {

    private final CvService cvService;

    @PostMapping(value = "/api/admin/cv", consumes = "multipart/form-data")
    public ResponseEntity<CvFile> uploadCv(@RequestPart("file") MultipartFile file)
            throws Exception {
        return ResponseEntity.ok(cvService.upload(file));
    }

    @GetMapping("/api/public/cv")
    public ResponseEntity<byte[]> getCv() throws IOException {
        CvFile cv = cvService.getCurrentCv();

        // Fetch the file from Cloudinary
        URL url = new URL(cv.getUrl());
        byte[] fileContent = url.openStream().readAllBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cv.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileContent);
    }

    @RequestMapping(value = "/api/public/cv", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headCv() {
        boolean exists = cvService.getCurrentCvOptional().isPresent();
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
