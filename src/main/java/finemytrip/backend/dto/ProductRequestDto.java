package finemytrip.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String tripType;
    private String title;
    private Integer discountRate;
    private String infoGroup;
    private Integer prevPrice;
    private Integer currPrice;
    private Double rating;
    private Integer sold;
    
    private MultipartFile imgSrc;
} 