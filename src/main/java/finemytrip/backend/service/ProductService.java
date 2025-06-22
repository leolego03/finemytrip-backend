package finemytrip.backend.service;

import finemytrip.backend.dto.ProductRequestDto;
import finemytrip.backend.dto.ProductResponseDto;
import finemytrip.backend.entity.Product;
import finemytrip.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = Product.builder()
                .title(requestDto.getTitle())
                .originalPrice(requestDto.getOriginalPrice())
                .discountedPrice(requestDto.getDiscountedPrice())
                .discountRate(requestDto.getDiscountRate())
                .discountBadge(requestDto.getDiscountBadge())
                .image(requestDto.getImage())
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
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found. ID: " + id));
        product.setImage(requestDto.getImage());
        product.setDiscountBadge(requestDto.getDiscountBadge());
        product.setTitle(requestDto.getTitle());
        product.setInfo(requestDto.getInfo());
        product.setDiscountRate(requestDto.getDiscountRate());
        product.setOriginalPrice(requestDto.getOriginalPrice());
        product.setDiscountedPrice(requestDto.getDiscountedPrice());
        product.setStarsHtml(requestDto.getStarsHtml());
        product.setReviewRate(requestDto.getReviewRate());
        product.setPurchaseCount(requestDto.getPurchaseCount());
        Product updated = productRepository.save(product);
        return convertToResponseDto(updated);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found. ID: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponseDto convertToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .image(product.getImage())
                .discountBadge(product.getDiscountBadge())
                .title(product.getTitle())
                .info(product.getInfo())
                .discountRate(product.getDiscountRate())
                .originalPrice(product.getOriginalPrice())
                .discountedPrice(product.getDiscountedPrice())
                .starsHtml(product.getStarsHtml())
                .reviewRate(product.getReviewRate())
                .purchaseCount(product.getPurchaseCount())
                .build();
    }
} 