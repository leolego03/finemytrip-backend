package finemytrip.backend.config;

import finemytrip.backend.entity.MainSlide;
import finemytrip.backend.repository.MainSlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    
    private final MainSlideRepository mainSlideRepository;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sample data loading is disabled. Starting with empty database.");
    }
} 