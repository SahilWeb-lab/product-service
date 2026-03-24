package com.ecom.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dto.ProductRequest;
import com.ecom.dto.ProductResponse;
import com.ecom.exception.ResourceNotFoundException;
import com.ecom.exception.ResourceStateException;
import com.ecom.mapper.PageResponseMapper;
import com.ecom.mapper.ProductMapper;
import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductAdminService;
import com.ecom.validation.ProductValidation;
import com.micro.payload.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductAdminServiceImpl implements ProductAdminService {

	private final ProductRepository productRepository;
	private final ProductValidation productValidation;
	private final ProductMapper productMapper;

	@Override
	@Transactional
	public ProductResponse createProduct(ProductRequest request) {

	    log.info("CreateProduct request: name={}, categoryId={}, brandId={}, actualPrice={}, discountedPrice={}",
	            request.getName(), request.getCategoryId(), request.getBrandId(),
	            request.getActualPrice(), request.getDiscountedPrice());

	    // Validation (uncomment when implemented)
	    // productValidation.validateProduct(request);

	    Product product = productMapper.toEntity(request);
	    Product savedProduct = productRepository.save(product);

	    log.info("Product created successfully: id={}, name={}", savedProduct.getId(), savedProduct.getName());

	    return productMapper.toDTO(savedProduct);
	}

	@Override
	public PageResponse<ProductResponse> getAllProducts(Integer pageNo, Integer pageSize, String sortBy,
	        String sortDir, String status) {

	    log.info("GetAllProducts request: pageNo={}, pageSize={}, sortBy={}, sortDir={}, status={}",
	            pageNo, pageSize, sortBy, sortDir, status);

	    Direction sortDirection = sortDir.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
	    Sort sort = Sort.by(sortDirection, sortBy);
	    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

	    Page<Product> productPage;

	    if (status.equalsIgnoreCase("active"))
	        productPage = productRepository.findByStatusTrue(pageable);
	    else if (status.equalsIgnoreCase("inactive"))
	        productPage = productRepository.findByStatusFalse(pageable);
	    else
	        productPage = productRepository.findAll(pageable);

	    log.info("GetAllProducts result: totalElements={}", productPage.getTotalElements());

	    return pageResponseMapper(productPage, sortBy, sortDir);
	}

	@Override
	@Transactional
	public ProductResponse updateProduct(Long id, ProductRequest request) {

	    log.info("UpdateProduct request: id={}, name={}, categoryId={}, brandId={}",
	            id, request.getName(), request.getCategoryId(), request.getBrandId());

	    Product existingProduct = getExistingProduct(id);

	    existingProduct.setActualPrice(request.getActualPrice());
	    existingProduct.setName(request.getName());
	    existingProduct.setDescription(request.getDescription());
	    existingProduct.setBrandId(request.getBrandId());
	    existingProduct.setCategoryId(request.getCategoryId());
	    existingProduct.setDiscountedPrice(request.getDiscountedPrice());
	    existingProduct.setStatus(request.getStatus());

	    Product savedProduct = productRepository.save(existingProduct);

	    log.info("Product updated successfully: id={}, name={}", savedProduct.getId(), savedProduct.getName());

	    return productMapper.toDTO(savedProduct);
	}

	@Override
	public void activateProduct(Long id) {

	    log.info("ActivateProduct request: productId={}", id);

	    Product existingProduct = getExistingProduct(id);

	    if (existingProduct.getStatus()) {
	        log.warn("Product already active: productId={}", id);
	        throw new ResourceStateException("Product already activated!");
	    }

	    existingProduct.setStatus(true);
	    productRepository.save(existingProduct);

	    log.info("Product activated successfully: productId={}", id);
	}

	@Override
	public void deactivateProduct(Long id) {

	    log.info("DeactivateProduct request: productId={}", id);

	    Product existingProduct = getExistingProduct(id);

	    if (!existingProduct.getStatus()) {
	        log.warn("Product already inactive: productId={}", id);
	        throw new ResourceStateException("Product already deactivated!");
	    }

	    existingProduct.setStatus(false);
	    productRepository.save(existingProduct);

	    log.info("Product deactivated successfully: productId={}", id);
	}

	@Override
	public void deleteProduct(Long id) {

	    log.info("DeleteProduct request: productId={}", id);

	    getExistingProduct(id);
	    productRepository.deleteById(id);

	    log.info("Product deleted successfully: productId={}", id);
	}
	
	@Override
	public PageResponse<ProductResponse> getProductsByCategory(Long categoryId, Integer pageNo, Integer pageSize,
			String sortBy, String sortDir, String status) {
		log.info("Fetching products by category: categoryId={}, pageNo={}, pageSize={}, sortBy={}, sortDir={}, status={}, ", categoryId, pageNo, pageSize, sortBy, sortDir, status);
//		Sort:
//		Sort Direction:
		Direction sortDirection = sortDir.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);

		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<Product> productPage = null;
		if(status.equalsIgnoreCase("active"))
			productPage = productRepository.findByCategoryIdAndStatusTrue(categoryId, pageable);
		else if(status.equalsIgnoreCase("inactive"))
			productPage = productRepository.findByCategoryIdAndStatusFalse(categoryId, pageable);
		else 
			productPage = productRepository.findByCategoryId(categoryId, pageable);
		
		log.info("Products fetched: totalElements={}", productPage.getTotalElements());
		
		return pageResponseMapper(productPage, sortBy, sortDir);
	}

	@Override
	public PageResponse<ProductResponse> getProductsByBrand(Long brandId, Integer pageNo, Integer pageSize,
			String sortBy, String sortDir, String status) {
		
		log.info("Fetching products by brand: brandId={}, pageNo={}, pageSize={}, sortBy={}, sortDir={}, status={}, ", brandId, pageNo, pageSize, sortBy, sortDir, status);
		
//		Sort:
//		Sort Direction:
		Direction sortDirection = sortDir.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<Product> productPage = null;
		if(status.equalsIgnoreCase("active"))
			productPage = productRepository.findByBrandIdAndStatusTrue(brandId, pageable);
		else if(status.equalsIgnoreCase("inactive"))
			productPage = productRepository.findByBrandIdAndStatusFalse(brandId, pageable);
		else 
			productPage = productRepository.findByBrandId(brandId, pageable);
		
		log.info("Products fetched: totalElements={}", productPage.getTotalElements());
		
		return pageResponseMapper(productPage, sortBy, sortDir);
	}

	
	private PageResponse<ProductResponse> pageResponseMapper(Page<Product> page, String sortBy, String sortDir) {
		Page<ProductResponse> pageResponse = page.map(productMapper::toDTO);
		return PageResponseMapper.fromPage(pageResponse, sortBy, sortDir);
	}
	
	private Product getExistingProduct(Long productId) {

	    return productRepository.findById(productId)
	            .orElseThrow(() -> {
	                log.warn("Product not found: productId={}", productId);
	                return new ResourceNotFoundException(
	                        "Product not found with id : [" + productId + "]");
	            });
	}

}
