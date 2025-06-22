package finemytrip.backend.controller;

import finemytrip.backend.dto.MainSlideRequestDto;
import finemytrip.backend.dto.MainSlideResponseDto;
import finemytrip.backend.service.MainSlideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/main-slides")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MainSlideController {
    
    private final MainSlideService mainSlideService;
    
    @GetMapping("/admin")
    public ResponseEntity<List<MainSlideResponseDto>> getAllSlides() {
        List<MainSlideResponseDto> slides = mainSlideService.getAllSlides();
        return ResponseEntity.ok(slides);
    }
    
    @GetMapping
    public ResponseEntity<List<MainSlideResponseDto>> getActiveSlides() {
        List<MainSlideResponseDto> slides = mainSlideService.getActiveSlides();
        return ResponseEntity.ok(slides);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MainSlideResponseDto> getSlideById(@PathVariable Long id) {
        try {
            MainSlideResponseDto slide = mainSlideService.getSlideById(id);
            return ResponseEntity.ok(slide);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createSlide(@Valid @RequestBody MainSlideRequestDto requestDto) {
        try {
            MainSlideResponseDto createdSlide = mainSlideService.createSlide(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSlide);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSlide(@PathVariable Long id, @Valid @RequestBody MainSlideRequestDto requestDto) {
        try {
            MainSlideResponseDto updatedSlide = mainSlideService.updateSlide(id, requestDto);
            return ResponseEntity.ok(updatedSlide);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSlide(@PathVariable Long id) {
        try {
            mainSlideService.deleteSlide(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleSlideStatus(@PathVariable Long id) {
        try {
            MainSlideResponseDto updatedSlide = mainSlideService.toggleSlideStatus(id);
            return ResponseEntity.ok(updatedSlide);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 