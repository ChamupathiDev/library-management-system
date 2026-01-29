package com.lib.api.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Get the current working directory
        String workingDir = System.getProperty("user.dir");
        Path uploadDir;

        // Smart Logic: Match the FileService logic
        if (workingDir.endsWith("library-api")) {
            uploadDir = Paths.get("uploads");
        } else {
            uploadDir = Paths.get("library-api", "uploads");
        }

        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/api/books/images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}