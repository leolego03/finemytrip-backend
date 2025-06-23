package finemytrip.backend.config;

import finemytrip.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class DataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final Environment environment;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${file.upload.path}")
    private String uploadPath;
    @Value("${app.file.cleanup.enabled:false}")
    private boolean fileCleanupEnabled;

    public DataLoader(MemberRepository memberRepository, Environment environment) {
        this.memberRepository = memberRepository;
        this.environment = environment;
    }

    @Override
    public void run(String... args) throws Exception {
        // Organize upload folders only in a development environment
        if (fileCleanupEnabled) {
            cleanUploadDirectory();
        }
        System.out.println("Data loading completed.");
    }

    private void cleanUploadDirectory() {
        try {
            Path uploadDir = Paths.get(uploadPath);
            if (Files.exists(uploadDir)) {
                try (Stream<Path> paths = Files.walk(uploadDir)) {
                    paths.filter(Files::isRegularFile)
                         .forEach(file -> {
                             try {
                                 Files.delete(file);
                                 System.out.println("Deleted file: " + file);
                             } catch (Exception e) {
                                 System.err.println("Failed to delete file: " + file + " - " + e.getMessage());
                             }
                         });
                }
                System.out.println("Upload directory cleaned successfully");
            } else {
                Files.createDirectories(uploadDir);
                System.out.println("Upload directory created: " + uploadDir);
            }
        } catch (Exception e) {
            System.err.println("Failed to clean upload directory: " + e.getMessage());
        }
    }
}
