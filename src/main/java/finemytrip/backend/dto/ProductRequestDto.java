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
    private String title;
    private String originalPrice;
    private String discountedPrice;
    private String discountRate;
    private String discountBadge;
    private String info;
    private String purchaseCount;
    private String reviewRate;
    private String starsHtml;
    
    private MultipartFile image;
} 