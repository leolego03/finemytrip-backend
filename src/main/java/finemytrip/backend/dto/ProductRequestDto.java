package finemytrip.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String image;
    private String discountBadge;
    private String title;
    private String info;
    private String discountRate;
    private String originalPrice;
    private String discountedPrice;
    private String starsHtml;
    private String reviewRate;
    private String purchaseCount;
} 