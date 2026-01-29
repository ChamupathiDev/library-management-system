package com.lib.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    private final Path root;

    public FileService() {
        // Step 1: Get the current working directory
        String workingDir = System.getProperty("user.dir");
        log.info("Current Working Directory: {}", workingDir);

        // Step 2: Dynamically set the path
        // If we are in the parent folder, go into library-api/uploads
        // If we are already in library-api, just use uploads
        if (workingDir.endsWith("library-api")) {
            this.root = Paths.get("uploads");
        } else {
            this.root = Paths.get("library-api", "uploads");
        }

        // Step 3: Ensure the directory exists immediately on startup
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
                log.info("Initialization: Created uploads directory at {}", root.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Could not initialize storage folder", e);
        }
    }

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        try {
            // Generate unique filename to avoid Librarian overwriting files
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Security check: ensure path is still within root
            Path destination = this.root.resolve(filename).normalize().toAbsolutePath();
            if (!destination.getParent().equals(this.root.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            // Standard Way: Copy binary stream to the destination
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            log.info("Librarian uploaded file successfully: {}", filename);
            return filename;
        } catch (IOException e) {
            log.error("Failed to store file. Error: {}", e.getMessage(), e);
            throw new RuntimeException("Could not store the file: " + e.getMessage());
        }
    }
}