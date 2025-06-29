package finemytrip.backend.service;

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

        Product product = Product.builder()
                .title(requestDto.getTitle())
                .discountRate(requestDto.getDiscountRate())
                .infoGroup(requestDto.getInfoGroup())
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

        product.setTitle(requestDto.getTitle());
        product.setDiscountRate(requestDto.getDiscountRate());
        product.setInfoGroup(requestDto.getInfoGroup());
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

    private ProductResponseDto convertToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .imgSrc(product.getImgSrc())
                .discountRate(product.getDiscountRate())
                .title(product.getTitle())
                .infoGroup(product.getInfoGroup())
                .prevPrice(product.getPrevPrice())
                .currPrice(product.getCurrPrice())
                .rating(product.getRating())
                .sold(product.getSold())
                .build();
    }
} 