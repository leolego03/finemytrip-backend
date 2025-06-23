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
        if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
            imageUrl = fileUploadService.uploadFile(requestDto.getImage());
        }

        Product product = Product.builder()
                .title(requestDto.getTitle())
                .originalPrice(requestDto.getOriginalPrice())
                .discountedPrice(requestDto.getDiscountedPrice())
                .discountRate(requestDto.getDiscountRate())
                .discountBadge(requestDto.getDiscountBadge())
                .image(imageUrl)
                .info(requestDto.getInfo())
                .purchaseCount(requestDto.getPurchaseCount())
                .reviewRate(requestDto.getReviewRate())
                .starsHtml(requestDto.getStarsHtml())
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
        if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
            // Delete old image file if exists
            if (product.getImage() != null) {
                fileUploadService.deleteFile(product.getImage());
            }
            String imageUrl = fileUploadService.uploadFile(requestDto.getImage());
            product.setImage(imageUrl);
        }

        product.setTitle(requestDto.getTitle());
        product.setOriginalPrice(requestDto.getOriginalPrice());
        product.setDiscountedPrice(requestDto.getDiscountedPrice());
        product.setDiscountRate(requestDto.getDiscountRate());
        product.setDiscountBadge(requestDto.getDiscountBadge());
        product.setInfo(requestDto.getInfo());
        product.setPurchaseCount(requestDto.getPurchaseCount());
        product.setReviewRate(requestDto.getReviewRate());
        product.setStarsHtml(requestDto.getStarsHtml());

        Product updatedProduct = productRepository.save(product);
        return convertToResponseDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found. ID: " + id));

        // Delete associated file
        if (product.getImage() != null) {
            fileUploadService.deleteFile(product.getImage());
        }

        productRepository.deleteById(id);
    }

    private ProductResponseDto convertToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .originalPrice(product.getOriginalPrice())
                .discountedPrice(product.getDiscountedPrice())
                .discountRate(product.getDiscountRate())
                .discountBadge(product.getDiscountBadge())
                .image(product.getImage())
                .info(product.getInfo())
                .purchaseCount(product.getPurchaseCount())
                .reviewRate(product.getReviewRate())
                .starsHtml(product.getStarsHtml())
                .build();
    }
} 