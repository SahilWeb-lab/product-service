package com.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ecom.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

//	USER SIDE:
//	====================================================
//	Create a method to get product by id: only active
	Optional<Product> findByIdAndStatusTrue(Long productId);

	Page<Product> findByStatusTrue(Pageable pageable);

	Page<Product> findByCategoryIdAndStatusTrue(Long categoryId, Pageable pageable);

	Page<Product> findByBrandIdAndStatusTrue(Long brandId, Pageable pageable);

	// Get multiple active products by IDs
	List<Product> findByIdInAndStatusTrue(List<Long> productIds);
	
//	ADMIN SIDE:
//	======================================================
	Page<Product> findByStatusFalse(Pageable pageable);
	
	Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
	
	Page<Product> findByBrandId(Long brandId, Pageable pageable);

	Page<Product> findByBrandIdAndStatusFalse(Long brandId, Pageable pageable);
	
	Page<Product> findByCategoryIdAndStatusFalse(Long categoryId, Pageable pageable);
}
