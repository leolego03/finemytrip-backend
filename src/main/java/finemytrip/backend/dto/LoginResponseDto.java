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
public class LoginResponseDto {
    private String token;
    private Long id;
    private String email;
    private Boolean marketingAgreed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 