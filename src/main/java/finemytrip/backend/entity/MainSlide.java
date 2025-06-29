package finemytrip.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "main_slides")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainSlide {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String url;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String headline;
    
    private String date;
    
    @Column(name = "img_src")
    private String imgSrc;
    
    @Column(name = "img_alt")
    private String imgAlt;
    
    @Column(name = "bg_src")
    private String bgSrc;
} 