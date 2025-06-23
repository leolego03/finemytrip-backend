package finemytrip.backend.service;

import finemytrip.backend.dto.MainSlideRequestDto;
import finemytrip.backend.dto.MainSlideResponseDto;
import finemytrip.backend.entity.MainSlide;
import finemytrip.backend.repository.MainSlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainSlideService {
    
    private final MainSlideRepository mainSlideRepository;
    private final FileUploadService fileUploadService;
    
    public List<MainSlideResponseDto> getAllSlides() {
        return mainSlideRepository.findAllByOrderBySortOrderAscCreatedAtAsc()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public List<MainSlideResponseDto> getActiveSlides() {
        return mainSlideRepository.findAllActiveOrderBySortOrder()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public MainSlideResponseDto getSlideById(Long id) {
        MainSlide slide = mainSlideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slide not found. ID: " + id));
        return convertToResponseDto(slide);
    }
    
    @Transactional
    public MainSlideResponseDto createSlide(MainSlideRequestDto requestDto) throws IOException {
        String imageUrl = requestDto.getImage();
        String bgImageUrl = requestDto.getBgImage();

        // Handle file uploads
        if (requestDto.getImageFile() != null && !requestDto.getImageFile().isEmpty()) {
            imageUrl = fileUploadService.uploadFile(requestDto.getImageFile());
        }
        if (requestDto.getBgImageFile() != null && !requestDto.getBgImageFile().isEmpty()) {
            bgImageUrl = fileUploadService.uploadFile(requestDto.getBgImageFile());
        }

        MainSlide slide = MainSlide.builder()
                .title(requestDto.getTitle())
                .headline(requestDto.getHeadline())
                .image(imageUrl)
                .bgImage(bgImageUrl)
                .url(requestDto.getUrl() != null ? requestDto.getUrl() : "#")
                .date(requestDto.getDate())
                .isActive(requestDto.getIsActive() != null ? requestDto.getIsActive() : true)
                .sortOrder(requestDto.getSortOrder())
                .build();
        
        MainSlide savedSlide = mainSlideRepository.save(slide);
        return convertToResponseDto(savedSlide);
    }
    
    @Transactional
    public MainSlideResponseDto updateSlide(Long id, MainSlideRequestDto requestDto) throws IOException {
        MainSlide slide = mainSlideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slide not found. ID: " + id));
        
        // Handle file uploads
        if (requestDto.getImageFile() != null && !requestDto.getImageFile().isEmpty()) {
            // Delete old image file if exists
            if (slide.getImage() != null) {
                fileUploadService.deleteFile(slide.getImage());
            }
            String imageUrl = fileUploadService.uploadFile(requestDto.getImageFile());
            slide.setImage(imageUrl);
        } else if (requestDto.getImage() != null) {
            slide.setImage(requestDto.getImage());
        }

        if (requestDto.getBgImageFile() != null && !requestDto.getBgImageFile().isEmpty()) {
            // Delete old background image file if exists
            if (slide.getBgImage() != null) {
                fileUploadService.deleteFile(slide.getBgImage());
            }
            String bgImageUrl = fileUploadService.uploadFile(requestDto.getBgImageFile());
            slide.setBgImage(bgImageUrl);
        } else if (requestDto.getBgImage() != null) {
            slide.setBgImage(requestDto.getBgImage());
        }

        slide.setTitle(requestDto.getTitle());
        slide.setHeadline(requestDto.getHeadline());
        slide.setUrl(requestDto.getUrl());
        slide.setDate(requestDto.getDate());
        slide.setIsActive(requestDto.getIsActive());
        slide.setSortOrder(requestDto.getSortOrder());
        
        MainSlide updatedSlide = mainSlideRepository.save(slide);
        return convertToResponseDto(updatedSlide);
    }
    
    @Transactional
    public void deleteSlide(Long id) throws IOException {
        MainSlide slide = mainSlideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slide not found. ID: " + id));

        // Delete associated files
        if (slide.getImage() != null) {
            fileUploadService.deleteFile(slide.getImage());
        }
        if (slide.getBgImage() != null) {
            fileUploadService.deleteFile(slide.getBgImage());
        }

        mainSlideRepository.deleteById(id);
    }
    
    @Transactional
    public MainSlideResponseDto toggleSlideStatus(Long id) {
        MainSlide slide = mainSlideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slide not found. ID: " + id));
        
        slide.setIsActive(!slide.getIsActive());
        MainSlide updatedSlide = mainSlideRepository.save(slide);
        return convertToResponseDto(updatedSlide);
    }
    
    private MainSlideResponseDto convertToResponseDto(MainSlide slide) {
        return MainSlideResponseDto.builder()
                .id(slide.getId())
                .title(slide.getTitle())
                .headline(slide.getHeadline())
                .image(slide.getImage())
                .bgImage(slide.getBgImage())
                .url(slide.getUrl())
                .date(slide.getDate())
                .isActive(slide.getIsActive())
                .sortOrder(slide.getSortOrder())
                .createdAt(slide.getCreatedAt())
                .updatedAt(slide.getUpdatedAt())
                .build();
    }
} 