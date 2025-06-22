package finemytrip.backend.repository;

import finemytrip.backend.entity.MainSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MainSlideRepository extends JpaRepository<MainSlide, Long> {
    
    @Query("SELECT m FROM MainSlide m WHERE m.isActive = true ORDER BY m.sortOrder ASC, m.createdAt ASC")
    List<MainSlide> findAllActiveOrderBySortOrder();
    
    List<MainSlide> findAllByOrderBySortOrderAscCreatedAtAsc();
} 