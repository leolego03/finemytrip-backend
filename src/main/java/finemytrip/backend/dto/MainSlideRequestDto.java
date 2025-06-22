package finemytrip.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainSlideRequestDto {
    
    @NotBlank(message = "URL is required")
    private String url;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String headline;
    
    private String date;
    
    private String image;
    
    private String bgImage;
    
    private Integer sortOrder;
    
    private Boolean isActive = true;
} 