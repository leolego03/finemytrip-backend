package finemytrip.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Slf4j
public class BackendApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener() {
        return event -> {
            log.info("=== Start Application - Check File Upload Settings ===");
            log.info("Spring Boot Version: {}", org.springframework.boot.SpringBootVersion.getVersion());
            log.info("Current Profile: {}", String.join(", ", environment.getActiveProfiles()));
            
            // Logging Actual File Upload Settings
            log.info("=== File upload settings (actual applied values) ===");
            String maxFileSize = environment.getProperty("spring.servlet.multipart.max-file-size", "No Defaults");
            String maxRequestSize = environment.getProperty("spring.servlet.multipart.max-request-size", "No Defaults");
            String multipartEnabled = environment.getProperty("spring.servlet.multipart.enabled", "true");
            String fileSizeThreshold = environment.getProperty("spring.servlet.multipart.file-size-threshold", "0B");
            String location = environment.getProperty("spring.servlet.multipart.location", "null");
            String resolveLazily = environment.getProperty("spring.servlet.multipart.resolve-lazily", "false");
            
            log.info("spring.servlet.multipart.max-file-size: {}", maxFileSize);
            log.info("spring.servlet.multipart.max-request-size: {}", maxRequestSize);
            log.info("spring.servlet.multipart.enabled: {}", multipartEnabled);
            log.info("spring.servlet.multipart.file-size-threshold: {}", fileSizeThreshold);
            log.info("spring.servlet.multipart.location: {}", location);
            log.info("spring.servlet.multipart.resolve-lazily: {}", resolveLazily);
            
            // Verifying Tomcat Settings
            String maxHttpFormPostSize = environment.getProperty("server.tomcat.max-http-form-post-size", "No Defaults");
            String maxSwallowSize = environment.getProperty("server.tomcat.max-swallow-size", "No Defaults");
            log.info("server.tomcat.max-http-form-post-size: {}", maxHttpFormPostSize);
            log.info("server.tomcat.max-swallow-size: {}", maxSwallowSize);
            
            // Verifying File Upload Path Settings
            String uploadPath = environment.getProperty("file.upload.path", System.getProperty("user.dir") + "/uploads");
            String uploadUrlPrefix = environment.getProperty("file.upload.url-prefix", "/uploads");
            log.info("file.upload.path: {}", uploadPath);
            log.info("file.upload.url-prefix: {}", uploadUrlPrefix);
            
            // Logging System Information
            log.info("=== System Information ===");
            log.info("JVM Memory information:");
            log.info("  - Maximum heap memory: {} MB", 
                String.format("%.2f", Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0)));
            log.info("  - Total heap memory: {} MB", 
                String.format("%.2f", Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0)));
            log.info("  - Available heap memory: {} MB", 
                String.format("%.2f", Runtime.getRuntime().freeMemory() / (1024.0 * 1024.0)));
            
            log.info("=== Application startup completed ===");
        };
    }
}
