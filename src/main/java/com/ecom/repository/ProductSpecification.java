package com.ecom.repository;

import org.springframework.data.jpa.domain.Specification;

import com.ecom.dto.ProductFilterRequest;
import com.ecom.model.Product;

import jakarta.persistence.criteria.Predicate;


public class ProductSpecification {

    public static Specification<Product> filter(ProductFilterRequest req) {
        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();

            Long brandId = req.getBrandId();
            Long categoryId = req.getCategoryId();
            String keyword = req.getKeyword();
            Double maxPrice = req.getMaxPrice();
            Double minPrice = req.getMinPrice();
            Boolean status = req.getStatus();

            // Keyword search (name + description)
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase().trim() + "%";

                Predicate nameMatch = cb.like(
                        cb.lower(root.get("name")), likePattern);

                Predicate descMatch = cb.like(
                        cb.lower(root.get("description")), likePattern);

                predicate = cb.and(predicate, cb.or(nameMatch, descMatch));
            }

            // Category filter
            if (categoryId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("categoryId"), categoryId));
            }

            //  Brand filter
            if (brandId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("brandId"), brandId));
            }

            // Price filters (flexible)
            if (minPrice != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("discountedPrice"), minPrice));
            }

            if (maxPrice != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("discountedPrice"), maxPrice));
            }

            // Status filter
            if (status != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), status));
            }

            return predicate;
        };
    }
}
