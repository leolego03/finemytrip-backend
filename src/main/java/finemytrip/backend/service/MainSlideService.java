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
        return mainSlideRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public List<MainSlideResponseDto> getActiveSlides() {
        return mainSlideRepository.findAll()
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
        String imgSrc = null;
        String bgSrc = null;

        // Handle file uploads
        if (requestDto.getImgSrc() != null && !requestDto.getImgSrc().isEmpty()) {
            imgSrc = fileUploadService.uploadFile(requestDto.getImgSrc());
        }
        
        if (requestDto.getBgSrc() != null && !requestDto.getBgSrc().isEmpty()) {
            bgSrc = fileUploadService.uploadFile(requestDto.getBgSrc());
        }

        MainSlide slide = MainSlide.builder()
                .title(requestDto.getTitle())
                .headline(requestDto.getHeadline())
                .imgSrc(imgSrc)
                .imgAlt(requestDto.getImgAlt())
                .bgSrc(bgSrc)
                .url(requestDto.getUrl() != null ? requestDto.getUrl() : "#")
                .date(requestDto.getDate())
                .build();
        
        MainSlide savedSlide = mainSlideRepository.save(slide);
        return convertToResponseDto(savedSlide);
    }
    
    @Transactional
    public MainSlideResponseDto updateSlide(Long id, MainSlideRequestDto requestDto) throws IOException {
        MainSlide slide = mainSlideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slide not found. ID: " + id));
        
        // Handle file uploads
        if (requestDto.getImgSrc() != null && !requestDto.getImgSrc().isEmpty()) {
            // Delete old image file if exists
            if (slide.getImgSrc() != null) {
                fileUploadService.deleteFile(slide.getImgSrc());
            }
            String imgSrc = fileUploadService.uploadFile(requestDto.getImgSrc());
            slide.setImgSrc(imgSrc);
        }

        if (requestDto.getBgSrc() != null && !requestDto.getBgSrc().isEmpty()) {
            // Delete old background image file if exists
            if (slide.getBgSrc() != null) {
                fileUploadService.deleteFile(slide.getBgSrc());
            }
            String bgSrc = fileUploadService.uploadFile(requestDto.getBgSrc());
            slide.setBgSrc(bgSrc);
        }

        slide.setTitle(requestDto.getTitle());
        slide.setHeadline(requestDto.getHeadline());
        slide.setImgAlt(requestDto.getImgAlt());
        slide.setUrl(requestDto.getUrl());
        slide.setDate(requestDto.getDate());
        
        MainSlide updatedSlide = mainSlideRepository.save(slide);
        return convertToResponseDto(updatedSlide);
    }
    
    @Transactional
    public void deleteSlide(Long id) throws IOException {
        MainSlide slide = mainSlideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slide not found. ID: " + id));

        // Delete associated files
        if (slide.getImgSrc() != null) {
            fileUploadService.deleteFile(slide.getImgSrc());
        }
        if (slide.getBgSrc() != null) {
            fileUploadService.deleteFile(slide.getBgSrc());
        }

        mainSlideRepository.deleteById(id);
    }
    
    private MainSlideResponseDto convertToResponseDto(MainSlide slide) {
        return MainSlideResponseDto.builder()
                .id(slide.getId())
                .title(slide.getTitle())
                .headline(slide.getHeadline())
                .imgSrc(slide.getImgSrc())
                .imgAlt(slide.getImgAlt())
                .bgSrc(slide.getBgSrc())
                .url(slide.getUrl())
                .date(slide.getDate())
                .build();
    }
} 