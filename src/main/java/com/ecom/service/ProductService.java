package com.ecom.service;

import java.util.List;

import com.ecom.dto.ProductFilterRequest;
import com.ecom.dto.ProductResponse;
import com.micro.payload.PageResponse;
//import com.micro.payload.PageResponse;

public interface ProductService {

	ProductResponse getProductById(Long id);

	PageResponse<ProductResponse> getAllActiveProducts(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

	PageResponse<ProductResponse> getProductsByCategory(Long categoryId, Integer pageNo, Integer pageSize,
			String sortBy, String sortDir);

	PageResponse<ProductResponse> getProductsByBrand(Long brandId, Integer pageNo, Integer pageSize, String sortBy,
			String sortDir);

//  Create a method to get product by multiple ids:
	List<ProductResponse> getAllProductByIds(List<Long> ids);
	
//	Get filtered products:
	PageResponse<ProductResponse> getFilteredProducts(ProductFilterRequest req, Integer pageNo, Integer pageSize, String sortBy,
			String sortDir);

}