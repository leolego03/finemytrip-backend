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
public class ProductResponseDto {
    private Long id;
    private String tripType;
    private String imgSrc;
    private Integer discountRate;
    private String title;
    private String infoGroup;
    private Integer prevPrice;
    private Integer currPrice;
    private Double rating;
    private Integer sold;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 