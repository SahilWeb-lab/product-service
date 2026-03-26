package com.ecom.endpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.dto.ProductFilterRequest;
import com.ecom.dto.ProductResponse;
import com.micro.payload.ApiResponse;
import com.micro.payload.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RequestMapping("api/v1/user/products")
@Tag(name = "User Product APIs", description = "APIs for users to browse and search products")
public interface ProductUserControllerEndpoint {

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by ID", description = "Fetch a single product using its unique ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId);

    // Get active products:
    @GetMapping("/active")
    @Operation(summary = "Get active products", description = "Fetch paginated list of all active products")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getActiveProducts(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") Integer pageNo,

            @Parameter(description = "Number of records per page")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @Parameter(description = "Field to sort by")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: asc or desc")
            @RequestParam(defaultValue = "asc") String sortDir);

    // Get product by brand:
    @GetMapping("/brand/{brandId}")
    @Operation(summary = "Get products by brand", description = "Fetch products filtered by brand with pagination")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductByBrand(
            @Parameter(description = "Brand ID")
            @PathVariable Long brandId,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") Integer pageNo,

            @Parameter(description = "Number of records per page")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @Parameter(description = "Field to sort by")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: asc or desc")
            @RequestParam(defaultValue = "asc") String sortDir);

    // Get product by category:
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category", description = "Fetch products filtered by category with pagination")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductByCategory(
            @Parameter(description = "Category ID")
            @PathVariable Long categoryId,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") Integer pageNo,

            @Parameter(description = "Number of records per page")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @Parameter(description = "Field to sort by")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: asc or desc")
            @RequestParam(defaultValue = "asc") String sortDir);

    // Filter products:
    @GetMapping("/filter")
    @Operation(summary = "Filter products", description = "Filter products using multiple criteria like price, brand, category, etc.")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getFilteredProduct(
            @Parameter(description = "Filter request body")
            @RequestBody ProductFilterRequest request,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") Integer pageNo,

            @Parameter(description = "Number of records per page")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @Parameter(description = "Field to sort by")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: asc or desc")
            @RequestParam(defaultValue = "asc") String sortDir);

     // testing:
	@GetMapping("/test/hello")
	public String hello();

}