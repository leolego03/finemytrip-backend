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

    private String imgSrc;
    private Integer discountRate;
    private String title;
    
    @ElementCollection
    @CollectionTable(name = "product_info_groups", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "info_group")
    private java.util.List<String> infoGroup;
    
    private Integer prevPrice;
    private Integer currPrice;
    private Double rating;
    private Integer sold;
} 