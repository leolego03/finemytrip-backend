package finemytrip.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainSlideRequestDto {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String headline;
    private String url;
    private String date;
    private String imgAlt;
    
    // File upload fields
    private MultipartFile imgSrc;
    private MultipartFile bgSrc;
} 