package com.ecom.service;

import com.ecom.dto.ProductRequest;
import com.ecom.dto.ProductResponse;
import com.micro.payload.PageResponse;

public interface ProductAdminService {

//	Create product:
	ProductResponse createProduct(ProductRequest request);

//	Get all products: Active + Inactive
	PageResponse<ProductResponse> getAllProducts(Integer pageNo, Integer pageSize, String sortBy, String sortDir,
			String status);

//	Update product:
	ProductResponse updateProduct(Long id, ProductRequest request);

//	Activate product:
	void activateProduct(Long id);

//	Deactivate product:
	void deactivateProduct(Long id);

//	Delete product:
	void deleteProduct(Long id);

	PageResponse<ProductResponse> getProductsByCategory(Long categoryId, Integer pageNo, Integer pageSize,
			String sortBy, String sortDir, String status);

	PageResponse<ProductResponse> getProductsByBrand(Long brandId, Integer pageNo, Integer pageSize, String sortBy,
			String sortDir, String status);

}
