package com.example.orientlamp_back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Serves uploaded files (images, etc.) as static resources under /uploads/**.
 *
 * Maps:  GET /uploads/**  →  <uploadDir>/**  (local filesystem)
 *
 * In production, once you switch FileStorageService to use a CDN / cloud bucket
 * you can remove this handler altogether.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Resolve to an absolute path so it works regardless of CWD
        String absoluteUploadPath = Paths.get(uploadDir).toAbsolutePath().normalize() + "/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absoluteUploadPath);
    }
}
