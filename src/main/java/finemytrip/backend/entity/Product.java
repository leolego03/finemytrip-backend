package finemytrip.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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