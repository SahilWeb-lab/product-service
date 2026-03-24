package com.ecom.mapper;

import org.springframework.stereotype.Component;

import com.ecom.dto.ProductRequest;
import com.ecom.dto.ProductResponse;
import com.ecom.model.Product;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {

        if (request == null) {
            return null;
        }

        return Product.builder()
        .name(request.getName())
        .description(request.getDescription())
        .actualPrice(request.getActualPrice())
        .discountedPrice(request.getDiscountedPrice())
        .brandId(request.getBrandId())
        .categoryId(request.getCategoryId())
        .status(request.getStatus())
        .build();
    }

    public ProductResponse toDTO(Product product) {

        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brandId(product.getBrandId())
                .categoryId(product.getCategoryId())
                .actualPrice(product.getActualPrice())
                .discountedPrice(product.getDiscountedPrice())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .updatedBy(product.getUpdatedBy())
                .build();
    }
}