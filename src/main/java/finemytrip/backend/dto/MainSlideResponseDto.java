package finemytrip.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainSlideResponseDto {
    
    private Long id;
    private String url;
    private String title;
    private String headline;
    private String date;
    private String image;
    private String bgImage;
    private Integer sortOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 