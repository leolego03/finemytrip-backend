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
    private String imgSrc;
    private String imgAlt;
    private String bgSrc;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 