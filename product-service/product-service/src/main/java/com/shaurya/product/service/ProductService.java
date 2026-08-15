package com.shaurya.product.service;

import com.shaurya.product.dto.ProductPageResponse;
import com.shaurya.product.dto.ProductRequest;
import com.shaurya.product.dto.ProductResponse;
import com.shaurya.product.entity.Product;
import com.shaurya.product.exception.ProductNotFoundException;
import com.shaurya.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest request){
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .category(request.category())
                .build();

        Product savedProduct = productRepository.save(product);

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getCategory(),
                savedProduct.getCreatedAt(),
                savedProduct.getUpdatedAt()
        );
    }

    public ProductResponse getProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }

    public ProductPageResponse getAllProducts(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);

        Page<ProductResponse> productResponses = products.map(product ->
                new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                )
        );
        return new ProductPageResponse(
                productResponses.getContent(),
                productResponses.getNumber(),
                productResponses.getSize(),
                productResponses.getTotalElements(),
                productResponses.getTotalPages(),
                productResponses.isFirst(),
                productResponses.isLast()
        );
    }
}
