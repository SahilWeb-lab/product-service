package com.ecom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.dto.ProductFilterRequest;
import com.ecom.dto.ProductResponse;
import com.ecom.endpoints.ProductUserControllerEndpoint;
import com.ecom.service.ProductService;
import com.ecom.util.ApiResponseUtil;
import com.micro.payload.ApiResponse;
import com.micro.payload.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductUserController implements ProductUserControllerEndpoint {

	private final ProductService productService;

	public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long productId) {

	    log.info("GetProductById request: productId={}", productId);

	    ProductResponse productResponse = productService.getProductById(productId);

	    log.info("GetProductById success: productId={}", productId);

	    return ApiResponseUtil.success(
	            productResponse,
	            "Product fetched successfully!",
	            HttpStatus.OK
	    );
	}

	public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getActiveProducts(
	        @RequestParam(defaultValue = "0") Integer pageNo,
	        @RequestParam(defaultValue = "10") Integer pageSize,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir) {

	    log.info("GetActiveProducts request: pageNo={}, pageSize={}, sortBy={}, sortDir={}",
	            pageNo, pageSize, sortBy, sortDir);

	    PageResponse<ProductResponse> pageResponse =
	            productService.getAllActiveProducts(pageNo, pageSize, sortBy, sortDir);

	    int returnedCount = pageResponse.getContent().size();

	    log.info("GetActiveProducts success: returnedCount={}, pageNo={}, pageSize={}",
	            returnedCount, pageNo, pageSize);

	    return ApiResponseUtil.success(
	            pageResponse,
	            returnedCount + " active products found!",
	            HttpStatus.OK
	    );
	}

	public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductByBrand(
	        @PathVariable Long brandId,
	        @RequestParam(defaultValue = "0") Integer pageNo,
	        @RequestParam(defaultValue = "10") Integer pageSize,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir) {

	    log.info("GetProductsByBrand request: brandId={}, pageNo={}, pageSize={}, sortBy={}, sortDir={}",
	            brandId, pageNo, pageSize, sortBy, sortDir);

	    PageResponse<ProductResponse> pageResponse =
	            productService.getProductsByBrand(brandId, pageNo, pageSize, sortBy, sortDir);

	    int returnedCount = pageResponse.getContent().size();

	    log.info("GetProductsByBrand success: brandId={}, returnedCount={}",
	            brandId, returnedCount);

	    return ApiResponseUtil.success(
	            pageResponse,
	            returnedCount + " products found!",
	            HttpStatus.OK
	    );
	}

	public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductByCategory(
	        @PathVariable Long categoryId,
	        @RequestParam(defaultValue = "0") Integer pageNo,
	        @RequestParam(defaultValue = "10") Integer pageSize,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir) {

	    log.info("GetProductsByCategory request: categoryId={}, pageNo={}, pageSize={}, sortBy={}, sortDir={}",
	            categoryId, pageNo, pageSize, sortBy, sortDir);

	    PageResponse<ProductResponse> pageResponse =
	            productService.getProductsByCategory(categoryId, pageNo, pageSize, sortBy, sortDir);

	    int returnedCount = pageResponse.getContent().size();

	    log.info("GetProductsByCategory success: categoryId={}, returnedCount={}",
	            categoryId, returnedCount);

	    return ApiResponseUtil.success(
	            pageResponse,
	            returnedCount + " products found!",
	            HttpStatus.OK
	    );
	}

	public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getFilteredProduct(
	        @RequestBody ProductFilterRequest request,
	        @RequestParam(defaultValue = "0") Integer pageNo,
	        @RequestParam(defaultValue = "10") Integer pageSize,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir) {

	    log.info("GetFilteredProducts request: keyword={}, categoryId={}, brandId={}, minPrice={}, maxPrice={}, pageNo={}, pageSize={}",
	            request.getKeyword(),
	            request.getCategoryId(),
	            request.getBrandId(),
	            request.getMinPrice(),
	            request.getMaxPrice(),
	            pageNo,
	            pageSize);

	    PageResponse<ProductResponse> filteredProducts =
	            productService.getFilteredProducts(request, pageNo, pageSize, sortBy, sortDir);

	    int returnedCount = filteredProducts.getContent().size();

	    log.info("GetFilteredProducts success: returnedCount={}", returnedCount);

	    return ApiResponseUtil.success(
	            filteredProducts,
	            returnedCount + " products found!",
	            HttpStatus.OK
	    );
	}

	public String hello() {
		return "Hello, Sahil Mandal!!"
	}
}