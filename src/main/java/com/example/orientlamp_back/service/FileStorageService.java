package com.example.orientlamp_back.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * Stores uploaded files on the local filesystem and returns the public URL
 * that can be embedded in entity records.
 *
 * Production note: swap the body of storeUniversityImage() to upload to
 * Cloudinary / AWS S3 / Azure Blob and return the CDN URL. The rest of the
 * codebase doesn't need to change.
 */
@Service
@Slf4j
public class FileStorageService {

    /** Root directory where uploaded files are written (can be a Docker volume). */
    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    /**
     * Base URL used to construct the public image URL returned to clients.
     * In production set this to your CDN / domain (e.g. https://api.orientlamp.ma).
     */
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    /**
     * Saves an image for a university and returns its publicly accessible URL.
     *
     * @param file the multipart image file
     * @param universityId used as a sub-folder to keep files organised
     * @return public URL, e.g. http://localhost:8080/uploads/universities/42/abc123.png
     */
    public String storeUniversityImage(MultipartFile file, Long universityId) {
        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "image"
        );

        // Derive extension from original name; fall back to .jpg
        String ext = "";
        int dotIdx = originalFilename.lastIndexOf('.');
        if (dotIdx >= 0) ext = originalFilename.substring(dotIdx);

        // Use a UUID so filenames never clash and no path-traversal is possible
        String storedFilename = UUID.randomUUID() + ext;

        Path targetDir = Paths.get(uploadDir, "universities", String.valueOf(universityId));
        try {
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), targetDir.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image for university " + universityId, e);
        }

        // Return the URL that Spring will serve via WebConfig
        return baseUrl + "/uploads/universities/" + universityId + "/" + storedFilename;
    }

    /**
     * Downloads an image from {@code sourceUrl}, stores it under the university's
     * uploads folder, and returns the public URL served by this application.
     * Returns {@code null} (and logs a warning) if the download fails for any reason.
     * Safe to call during seeding: failures never abort the caller.
     *
     * @param sourceUrl   remote URL to fetch (http or https)
     * @param universityId used to organise files in sub-folders
     * @param filename    desired filename including extension (e.g. "logo.png")
     */
    public String storeFromUrl(String sourceUrl, Long universityId, String filename) {
        if (sourceUrl == null || sourceUrl.isBlank()) return null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(sourceUrl).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 OrientLamp/1.0");
            conn.setConnectTimeout(6_000);
            conn.setReadTimeout(15_000);
            conn.setInstanceFollowRedirects(true);

            int status = conn.getResponseCode();
            if (status < 200 || status >= 300) {
                log.warn("Image download returned HTTP {} for {}", status, sourceUrl);
                return null;
            }

            Path targetDir = Paths.get(uploadDir, "universities", String.valueOf(universityId));
            Files.createDirectories(targetDir);

            try (InputStream in = conn.getInputStream()) {
                Files.copy(in, targetDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            }

            log.info("Stored seed image for university {}: {}", universityId, filename);
            return baseUrl + "/uploads/universities/" + universityId + "/" + filename;

        } catch (IOException e) {
            log.warn("Could not download seed image from {}: {}", sourceUrl, e.getMessage());
            return null;
        }
    }

    /**
     * Generates a branded SVG emblem (rounded rectangle + bold abbreviation text)
     * for a university and saves it as {@code logo.svg} in its uploads folder.
     * This is completely self-contained — no network call, no external dependency.
     *
     * @param universityId  used to organise files
     * @param abbreviation  1–4 character abbreviation shown on the emblem (e.g. "EMI")
     * @param bgColor       CSS/hex color for the background rectangle (e.g. "#1565C0")
     * @return the public served URL, or {@code null} on I/O failure
     */
    public String generateAndStoreSvgLogo(Long universityId, String abbreviation, String bgColor) {
        String text = abbreviation.length() > 4 ? abbreviation.substring(0, 4) : abbreviation;
        int fontSize = text.length() <= 2 ? 76 : text.length() == 3 ? 56 : 44;

        String svg = String.format(
            "<svg xmlns='http://www.w3.org/2000/svg' width='200' height='200' viewBox='0 0 200 200'>" +
            "<rect width='200' height='200' rx='28' fill='%s'/>" +
            "<text x='100' y='108' font-size='%d' font-family='Arial,Helvetica,sans-serif' " +
            "font-weight='bold' fill='white' text-anchor='middle' dominant-baseline='middle' " +
            "letter-spacing='-1'>%s</text></svg>",
            bgColor, fontSize, xmlEscape(text));

        try {
            Path targetDir = Paths.get(uploadDir, "universities", String.valueOf(universityId));
            Files.createDirectories(targetDir);
            Files.writeString(targetDir.resolve("logo.svg"), svg);
            return baseUrl + "/uploads/universities/" + universityId + "/logo.svg";
        } catch (IOException e) {
            log.warn("Could not generate SVG logo for university {}: {}", universityId, e.getMessage());
            return null;
        }
    }

    private static String xmlEscape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /** Deletes a previously stored file given its public URL. No-op if URL is external. */
    public void deleteByUrl(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith(baseUrl + "/uploads/")) return;
        String relativePath = imageUrl.substring((baseUrl + "/uploads/").length());
        Path filePath = Paths.get(uploadDir, relativePath);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Could not delete image file {}: {}", filePath, e.getMessage());
        }
    }
}
