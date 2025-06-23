package finemytrip.backend.controller;

import finemytrip.backend.dto.MainSlideRequestDto;
import finemytrip.backend.dto.MainSlideResponseDto;
import finemytrip.backend.service.MainSlideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/main-slides")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MainSlideController {
    
    private final MainSlideService mainSlideService;
    
    @GetMapping
    public ResponseEntity<List<MainSlideResponseDto>> getAllSlides() {
        List<MainSlideResponseDto> slides = mainSlideService.getAllSlides();
        return ResponseEntity.ok(slides);
    }
    
    @GetMapping("/admin")
    public ResponseEntity<List<MainSlideResponseDto>> getAdminSlides() {
        List<MainSlideResponseDto> slides = mainSlideService.getAllSlides();
        return ResponseEntity.ok(slides);
    }
    
    @GetMapping("/active")
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
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MainSlideResponseDto> createSlide(@ModelAttribute MainSlideRequestDto requestDto) {
        try {
            MainSlideResponseDto createdSlide = mainSlideService.createSlide(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSlide);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MainSlideResponseDto> updateSlide(@PathVariable Long id, @ModelAttribute MainSlideRequestDto requestDto) {
        try {
            MainSlideResponseDto updatedSlide = mainSlideService.updateSlide(id, requestDto);
            return ResponseEntity.ok(updatedSlide);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlide(@PathVariable Long id) {
        try {
            mainSlideService.deleteSlide(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<MainSlideResponseDto> toggleSlideStatus(@PathVariable Long id) {
        try {
            MainSlideResponseDto updatedSlide = mainSlideService.toggleSlideStatus(id);
            return ResponseEntity.ok(updatedSlide);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 