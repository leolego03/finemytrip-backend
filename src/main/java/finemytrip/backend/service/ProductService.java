package finemytrip.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import finemytrip.backend.dto.ProductRequestDto;
import finemytrip.backend.dto.ProductResponseDto;
import finemytrip.backend.entity.Product;
import finemytrip.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final FileUploadService fileUploadService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) throws IOException {
        String imageUrl = null;
        String introImageUrl = null;

        // Handle Base64 image upload
        if (requestDto.getImgSrc() != null && !requestDto.getImgSrc().isEmpty()) {
            imageUrl = uploadBase64Image(requestDto.getImgSrc());
        }

        // Handle Base64 intro image upload
        if (requestDto.getIntroImgSrc() != null && !requestDto.getIntroImgSrc().isEmpty()) {
            introImageUrl = uploadBase64Image(requestDto.getIntroImgSrc());
        }

        // Parse infoGroup from JSON string to List<String>
        List<String> infoGroupList = parseInfoGroup(requestDto.getInfoGroup());

        Product product = Product.builder()
                .tripType(requestDto.getTripType())
                .imgSrc(imageUrl)
                .discountRate(requestDto.getDiscountRate())
                .title(requestDto.getTitle())
                .infoGroup(infoGroupList)
                .prevPrice(requestDto.getPrevPrice())
                .currPrice(requestDto.getCurrPrice())
                .rating(requestDto.getRating())
                .sold(requestDto.getSold())
                .introTitle(requestDto.getIntroTitle())
                .introImgSrc(introImageUrl)
                .introText(requestDto.getIntroText())
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToResponseDto(savedProduct);
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found. ID: " + id));
        return convertToResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found. ID: " + id));

        // Handle Base64 image upload
        if (requestDto.getImgSrc() != null && !requestDto.getImgSrc().isEmpty()) {
            // Delete old image file if exists
            if (product.getImgSrc() != null) {
                fileUploadService.deleteFile(product.getImgSrc());
            }
            String imageUrl = uploadBase64Image(requestDto.getImgSrc());
            product.setImgSrc(imageUrl);
        }

        // Handle Base64 intro image upload
        if (requestDto.getIntroImgSrc() != null && !requestDto.getIntroImgSrc().isEmpty()) {
            // Delete old intro image file if exists
            if (product.getIntroImgSrc() != null) {
                fileUploadService.deleteFile(product.getIntroImgSrc());
            }
            String introImageUrl = uploadBase64Image(requestDto.getIntroImgSrc());
            product.setIntroImgSrc(introImageUrl);
        }

        // Parse infoGroup from JSON string to List<String>
        List<String> infoGroupList = parseInfoGroup(requestDto.getInfoGroup());

        product.setTripType(requestDto.getTripType());
        product.setDiscountRate(requestDto.getDiscountRate());
        product.setTitle(requestDto.getTitle());
        product.setInfoGroup(infoGroupList);
        product.setPrevPrice(requestDto.getPrevPrice());
        product.setCurrPrice(requestDto.getCurrPrice());
        product.setRating(requestDto.getRating());
        product.setSold(requestDto.getSold());
        product.setIntroTitle(requestDto.getIntroTitle());
        product.setIntroText(requestDto.getIntroText());

        Product updatedProduct = productRepository.save(product);
        return convertToResponseDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found. ID: " + id));

        // Delete associated files
        if (product.getImgSrc() != null) {
            fileUploadService.deleteFile(product.getImgSrc());
        }
        
        if (product.getIntroImgSrc() != null) {
            fileUploadService.deleteFile(product.getIntroImgSrc());
        }

        productRepository.deleteById(id);
    }

    // Method to upload Base64 image to file
    private String uploadBase64Image(String base64Image) throws IOException {
        try {
            // Remove header from Base64 data (e.g. "data:image/png;base64,")
            String base64Data = base64Image;
            if (base64Image.contains(",")) {
                base64Data = base64Image.substring(base64Image.indexOf(",") + 1);
            }
            
            // Base64 Decoding
            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Data);
            
            // Create File Name
            String fileName = UUID.randomUUID().toString() + ".png";
            
            // Convert to MultipartFile to use existing File Upload Service
            MultipartFile multipartFile = new MultipartFile() {
                @Override
                public String getName() {
                    return "file";
                }

                @Override
                public String getOriginalFilename() {
                    return fileName;
                }

                @Override
                public String getContentType() {
                    return "image/png";
                }

                @Override
                public boolean isEmpty() {
                    return imageBytes.length == 0;
                }

                @Override
                public long getSize() {
                    return imageBytes.length;
                }

                @Override
                public byte[] getBytes() throws IOException {
                    return imageBytes;
                }

                @Override
                public java.io.InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(imageBytes);
                }

                @Override
                public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                    java.nio.file.Files.write(dest.toPath(), imageBytes);
                }
            };
            
            return fileUploadService.uploadFile(multipartFile);
            
        } catch (Exception e) {
            log.error("Failed to upload Base64 image: {}", e.getMessage());
            throw new IOException("Failed to upload Base64 image", e);
        }
    }

    private List<String> parseInfoGroup(String infoGroupJson) {
        if (infoGroupJson == null || infoGroupJson.trim().isEmpty()) {
            return List.of();
        }
        
        try {
            return objectMapper.readValue(infoGroupJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // If JSON parsing fails, it is treated as a comma-separated string
            return List.of(infoGroupJson.split(","));
        }
    }

    private ProductResponseDto convertToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .tripType(product.getTripType())
                .imgSrc(product.getImgSrc())
                .discountRate(product.getDiscountRate())
                .title(product.getTitle())
                .infoGroup(product.getInfoGroup())
                .prevPrice(product.getPrevPrice())
                .currPrice(product.getCurrPrice())
                .rating(product.getRating())
                .sold(product.getSold())
                .introTitle(product.getIntroTitle())
                .introImgSrc(product.getIntroImgSrc())
                .introText(product.getIntroText())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}