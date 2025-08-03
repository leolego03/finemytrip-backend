package finemytrip.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String tripType;
    private String imgSrc;
    private Integer discountRate;
    private String title;
    private List<String> infoGroup;
    private Integer prevPrice;
    private Integer currPrice;
    private Double rating;
    private Integer sold;

    private String introTitle;
    private String introText;
    private String introImgSrc;
    
    // Securely handle all special characters in introText
    public String getIntroText() {
        if (introText == null) {
            return null;
        }
        // Replace opening characters, carriage returns, tabs with spaces
        // Safely handle special characters, too
        return introText
                .replaceAll("[\\r\\n\\t]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
    
    // Apply the same processing in setter
    public void setIntroText(String introText) {
        if (introText != null) {
            this.introText = introText
                    .replaceAll("[\\r\\n\\t]", " ")
                    .replaceAll("\\s+", " ")
                    .trim();
        } else {
            this.introText = null;
        }
    }
} 