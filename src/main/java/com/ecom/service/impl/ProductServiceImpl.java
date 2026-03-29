package com.ecom.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ecom.dto.ProductFilterRequest;
import com.ecom.dto.ProductResponse;
import com.ecom.exception.ResourceNotFoundException;
import com.ecom.mapper.PageResponseMapper;
import com.ecom.mapper.ProductMapper;
import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.ProductSpecification;
import com.ecom.service.ProductService;
import com.micro.payload.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	@Override
	public ProductResponse getProductById(Long id) {

	    log.info("Fetching product: productId={}", id);

	    Product product = getExistingProduct(id);

	    log.debug("Product found: productId={}, name={}", product.getId(), product.getName());

	    return productMapper.toDTO(product);
	}

	@Override
	public PageResponse<ProductResponse> getAllActiveProducts(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {

	    log.info("Fetching active products: pageNo={}, pageSize={}, sortBy={}, sortDir={}",
	            pageNo, pageSize, sortBy, sortDir);

	    Direction sortDirection = sortDir.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
	    Sort sort = Sort.by(sortDirection, sortBy);
	    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

	    Page<Product> page = productRepository.findByStatusTrue(pageable);

	    log.info("Fetched active products: totalElements={}", page.getTotalElements());

	    return pageResponseMapper(page, sortBy, sortDir);
	}

	@Override
	public PageResponse<ProductResponse> getProductsByCategory(Long categoryId, Integer pageNo, Integer pageSize,
	        String sortBy, String sortDir) {

	    log.info("Fetching products by category: categoryId={}, pageNo={}, pageSize={}",
	            categoryId, pageNo, pageSize);

	    Direction sortDirection = sortDir.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
	    Sort sort = Sort.by(sortDirection, sortBy);
	    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

	    Page<Product> page = productRepository.findByCategoryIdAndStatusTrue(categoryId, pageable);

	    log.info("Fetched products by category: categoryId={}, totalElements={}",
	            categoryId, page.getTotalElements());

	    return pageResponseMapper(page, sortBy, sortDir);
	}

	@Override
	public PageResponse<ProductResponse> getProductsByBrand(Long brandId, Integer pageNo, Integer pageSize,
	        String sortBy, String sortDir) {

	    log.info("Fetching products by brand: brandId={}, pageNo={}, pageSize={}",
	            brandId, pageNo, pageSize);

	    Direction sortDirection = sortDir.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
	    Sort sort = Sort.by(sortDirection, sortBy);
	    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

	    Page<Product> page = productRepository.findByBrandIdAndStatusTrue(brandId, pageable);

	    log.info("Fetched products by brand: brandId={}, totalElements={}",
	            brandId, page.getTotalElements());

	    return pageResponseMapper(page, sortBy, sortDir);
	}
	
	@Override
	public List<ProductResponse> getAllProductByIds(List<Long> ids) {

	    log.info("Fetching products by ids: count={}", ids != null ? ids.size() : 0);

	    if (ids == null || ids.isEmpty()) {
	        log.warn("Empty product id list received");
	        return List.of();
	    }

	    List<Product> products = productRepository.findByIdInAndStatusTrue(ids);

	    log.info("Products fetched: requestedCount={}, foundCount={}", ids.size(), products.size());

	    List<ProductResponse> productResponseList =
	            products.stream()
	                    .map(productMapper::toDTO)
	                    .toList();

	    return productResponseList;
	}
	
	@Override
	public PageResponse<ProductResponse> getFilteredProducts(ProductFilterRequest req,
	        Integer pageNo, Integer pageSize, String sortBy, String sortDir) {

	    log.info("Filtering products: keyword={}, categoryId={}, brandId={}, minPrice={}, maxPrice={}, status={}",
	            req.getKeyword(),
	            req.getCategoryId(),
	            req.getBrandId(),
	            req.getMinPrice(),
	            req.getMaxPrice(),
	            req.getStatus());

	    Specification<Product> specification = ProductSpecification.filter(req);

	    Direction sortDirection = sortDir.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
	    Sort sort = Sort.by(sortDirection, sortBy);
	    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

	    Page<Product> productPage = productRepository.findAll(specification, pageable);

	    log.info("Filtered products result: totalElements={}", productPage.getTotalElements());

	    return pageResponseMapper(productPage, sortBy, sortDir);
	}
	
	private PageResponse<ProductResponse> pageResponseMapper(Page<Product> page, String sortBy, String sortDir) {
		Page<ProductResponse> pageResponse = page.map(productMapper::toDTO);
		return PageResponseMapper.fromPage(pageResponse, sortBy, sortDir);
	}	
	
	private Product getExistingProduct(Long productId) {

	    return productRepository.findByIdAndStatusTrue(productId)
	            .orElseThrow(() -> {
	                log.warn("Product not found: productId={}", productId);
	                return new ResourceNotFoundException(
	                        "Product not found with id : [" + productId + "]");
	            });
	}

}
