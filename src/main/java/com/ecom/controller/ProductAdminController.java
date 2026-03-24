package com.ecom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.dto.ProductRequest;
import com.ecom.dto.ProductResponse;
import com.ecom.endpoints.ProductAdminControllerEndpoint;
import com.ecom.service.ProductAdminService;
import com.ecom.service.ProductService;
import com.ecom.util.ApiResponseUtil;
import com.micro.payload.ApiResponse;
import com.micro.payload.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductAdminController implements ProductAdminControllerEndpoint {

	private final ProductAdminService productService;

	public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
			@Valid @RequestBody ProductRequest productRequest) {

		log.info("CreateProduct request: name={}, categoryId={}, brandId={}", productRequest.getName(),
				productRequest.getCategoryId(), productRequest.getBrandId());

		ProductResponse productResponse = productService.createProduct(productRequest);

		log.info("Product created successfully: id={}", productResponse.getId());

		return ApiResponseUtil.success(productResponse, "Product created successfully!", HttpStatus.CREATED);
	}

//    Get all products:
	public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir,
			@RequestParam(defaultValue = "all") String status) {

		log.info("GetProducts request: pageNo={}, pageSize={}, sortBy={}, sortDir={}, status={}", pageNo, pageSize,
				sortBy, sortDir, status);

		PageResponse<ProductResponse> pageResponse = productService.getAllProducts(pageNo, pageSize, sortBy, sortDir,
				status);

		int totalProducts = pageResponse.getContent().size();

		log.info("GetProducts success: returnedCount={}, pageNo={}, pageSize={}", totalProducts, pageNo, pageSize);

		return ApiResponseUtil.success(pageResponse, totalProducts + " products found!", HttpStatus.OK);
	}

	// ==========================================
	// UPDATE
	// ==========================================
	public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long productId,
			@Valid @RequestBody ProductRequest productRequest) {

		log.info("UpdateProduct request: productId={}, name={}, categoryId={}, brandId={}", productId,
				productRequest.getName(), productRequest.getCategoryId(), productRequest.getBrandId());

		ProductResponse productResponse = productService.updateProduct(productId, productRequest);

		log.info("UpdateProduct success: productId={}", productId);

		return ApiResponseUtil.success(productResponse, "Product updated successfully!", HttpStatus.OK);
	}

//	Activate product:
	public ResponseEntity<ApiResponse<Void>> activateProduct(@PathVariable Long productId) {

		log.info("ActivateProduct request: productId={}", productId);

		productService.activateProduct(productId);

		log.info("ActivateProduct success: productId={}", productId);

		return ApiResponseUtil.success(null, "Product activated successfully!", HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse<Void>> deactivateProduct(@PathVariable Long productId) {

		log.info("DeactivateProduct request: productId={}", productId);

		productService.deactivateProduct(productId);

		log.info("DeactivateProduct success: productId={}", productId);

		return ApiResponseUtil.success(null, "Product deactivated successfully!", HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {

		log.info("DeleteProduct request: productId={}", productId);

		productService.deleteProduct(productId);

		log.info("DeleteProduct success: productId={}", productId);

		return ApiResponseUtil.success(null, "Product deleted successfully!", HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductByCategory(
			@PathVariable Long categoryId, @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir, @RequestParam(defaultValue = "all") String status) {

		log.info(
				"GetProductsByCategory request: categoryId={}, pageNo={}, pageSize={}, sortBy={}, sortDir={}, status={}",
				categoryId, pageNo, pageSize, sortBy, sortDir, status);

		PageResponse<ProductResponse> pageResponse = productService.getProductsByCategory(categoryId, pageNo, pageSize,
				sortBy, sortDir, status);

		int totalProducts = pageResponse.getContent().size();

		log.info("GetProductsByCategory success: categoryId={}, returnedCount={}", categoryId, totalProducts);

		return ApiResponseUtil.success(pageResponse, totalProducts + " products found!", HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductByBrand(@PathVariable Long brandId,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir,
			@RequestParam(defaultValue = "all") String status) {

		log.info("GetProductsByBrand request: brandId={}, pageNo={}, pageSize={}, sortBy={}, sortDir={}, status={}",
				brandId, pageNo, pageSize, sortBy, sortDir, status);

		PageResponse<ProductResponse> pageResponse = productService.getProductsByBrand(brandId, pageNo, pageSize,
				sortBy, sortDir, status);

		int returnedCount = pageResponse.getContent().size();

		log.info("GetProductsByBrand success: brandId={}, returnedCount={}", brandId, returnedCount);

		return ApiResponseUtil.success(pageResponse, returnedCount + " products found!", HttpStatus.OK);
	}

}
