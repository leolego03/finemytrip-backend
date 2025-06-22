package finemytrip.backend.service;

import finemytrip.backend.dto.MainSlideRequestDto;
import finemytrip.backend.dto.MainSlideResponseDto;
import finemytrip.backend.entity.MainSlide;
import finemytrip.backend.repository.MainSlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainSlideService {
    
    private final MainSlideRepository mainSlideRepository;
    
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
    public MainSlideResponseDto createSlide(MainSlideRequestDto requestDto) {
        MainSlide slide = MainSlide.builder()
                .title(requestDto.getTitle())
                .headline(requestDto.getHeadline())
                .image(requestDto.getImage())
                .bgImage(requestDto.getBgImage())
                .url(requestDto.getUrl())
                .date(requestDto.getDate())
                .isActive(requestDto.getIsActive())
                .sortOrder(requestDto.getSortOrder())
                .build();
        
        MainSlide savedSlide = mainSlideRepository.save(slide);
        return convertToResponseDto(savedSlide);
    }
    
    @Transactional
    public MainSlideResponseDto updateSlide(Long id, MainSlideRequestDto requestDto) {
        MainSlide slide = mainSlideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slide not found. ID: " + id));
        
        slide.setTitle(requestDto.getTitle());
        slide.setHeadline(requestDto.getHeadline());
        slide.setImage(requestDto.getImage());
        slide.setBgImage(requestDto.getBgImage());
        slide.setUrl(requestDto.getUrl());
        slide.setDate(requestDto.getDate());
        slide.setIsActive(requestDto.getIsActive());
        slide.setSortOrder(requestDto.getSortOrder());
        
        MainSlide updatedSlide = mainSlideRepository.save(slide);
        return convertToResponseDto(updatedSlide);
    }
    
    @Transactional
    public void deleteSlide(Long id) {
        if (!mainSlideRepository.existsById(id)) {
            throw new RuntimeException("Slide not found. ID: " + id);
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