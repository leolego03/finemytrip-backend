package finemytrip.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import finemytrip.backend.dto.ProductRequestDto;
import finemytrip.backend.dto.ProductResponseDto;
import finemytrip.backend.entity.Product;
import finemytrip.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

        // Handle file upload
        if (requestDto.getImgSrc() != null && !requestDto.getImgSrc().isEmpty()) {
            imageUrl = fileUploadService.uploadFile(requestDto.getImgSrc());
        }

        // Parse infoGroup from JSON string to List<String>
        List<String> infoGroupList = parseInfoGroup(requestDto.getInfoGroup());

        Product product = Product.builder()
                .tripType(requestDto.getTripType())
                .title(requestDto.getTitle())
                .discountRate(requestDto.getDiscountRate())
                .infoGroup(infoGroupList)
                .prevPrice(requestDto.getPrevPrice())
                .currPrice(requestDto.getCurrPrice())
                .rating(requestDto.getRating())
                .sold(requestDto.getSold())
                .imgSrc(imageUrl)
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

        // Handle file upload
        if (requestDto.getImgSrc() != null && !requestDto.getImgSrc().isEmpty()) {
            // Delete old image file if exists
            if (product.getImgSrc() != null) {
                fileUploadService.deleteFile(product.getImgSrc());
            }
            String imageUrl = fileUploadService.uploadFile(requestDto.getImgSrc());
            product.setImgSrc(imageUrl);
        }

        // Parse infoGroup from JSON string to List<String>
        List<String> infoGroupList = parseInfoGroup(requestDto.getInfoGroup());

        product.setTripType(requestDto.getTripType());
        product.setTitle(requestDto.getTitle());
        product.setDiscountRate(requestDto.getDiscountRate());
        product.setInfoGroup(infoGroupList);
        product.setPrevPrice(requestDto.getPrevPrice());
        product.setCurrPrice(requestDto.getCurrPrice());
        product.setRating(requestDto.getRating());
        product.setSold(requestDto.getSold());

        Product updatedProduct = productRepository.save(product);
        return convertToResponseDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found. ID: " + id));

        // Delete associated file
        if (product.getImgSrc() != null) {
            fileUploadService.deleteFile(product.getImgSrc());
        }

        productRepository.deleteById(id);
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
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
} 