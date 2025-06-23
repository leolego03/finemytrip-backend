package finemytrip.backend.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    @PostConstruct
    public void init() {
        createUploadDirectoryIfNotExists();
        log.info("FileUploadService initialized - uploadPath: '{}', urlPrefix: '{}'", uploadPath, urlPrefix);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Create upload directory if it doesn't exist
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path filePath = uploadDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);

        // Return URL path - trim any whitespace
        String urlPath = urlPrefix.trim() + "/" + uniqueFilename;
        log.info("File uploaded: {} -> URL: '{}'", originalFilename, urlPath);
        return urlPath;
    }

    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl == null || !fileUrl.startsWith(urlPrefix.trim())) {
            log.warn("Invalid file URL for deletion: {}", fileUrl);
            return;
        }

        String filename = fileUrl.substring(urlPrefix.trim().length() + 1);
        Path filePath = Paths.get(uploadPath, filename);
        
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("File deleted: {}", filePath);
        } else {
            log.warn("File not found for deletion: {}", filePath);
        }
    }

    private void createUploadDirectoryIfNotExists() {
        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                log.info("Upload directory created: {}", uploadDir.toAbsolutePath());
            } else {
                log.info("Upload directory already exists: {}", uploadDir.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Failed to create upload directory: {}", e.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
} 