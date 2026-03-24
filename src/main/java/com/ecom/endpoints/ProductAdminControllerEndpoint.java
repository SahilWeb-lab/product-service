package com.ecom.endpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.dto.ProductRequest;
import com.ecom.dto.ProductResponse;
import com.micro.payload.ApiResponse;
import com.micro.payload.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

@RequestMapping("api/v1/admin/products")
@Tag(name = "Admin Product APIs", description = "APIs for admin to manage products")
public interface ProductAdminControllerEndpoint {

    @PostMapping
    @Operation(summary = "Create product", description = "Admin can create a new product")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Parameter(description = "Product request payload", required = true)
            @Valid @RequestBody ProductRequest productRequest);

    // Get all products:
    @GetMapping
    @Operation(summary = "Get all products", description = "Fetch paginated list of products with optional status filter (all, active, inactive)")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") Integer pageNo,

            @Parameter(description = "Number of records per page")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @Parameter(description = "Field to sort by")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: asc or desc")
            @RequestParam(defaultValue = "asc") String sortDir,

            @Parameter(description = "Filter by status: all, active, inactive")
            @RequestParam(defaultValue = "all") String status);

    @PutMapping("/{productId}")
    @Operation(summary = "Update product", description = "Update existing product details by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(

            @Parameter(description = "Product ID")
            @PathVariable Long productId,

            @Parameter(description = "Updated product request payload")
            @Valid @RequestBody ProductRequest productRequest);

    @PatchMapping("/{productId}/activate")
    @Operation(summary = "Activate product", description = "Mark a product as active")
    public ResponseEntity<ApiResponse<Void>> activateProduct(

            @Parameter(description = "Product ID")
            @PathVariable Long productId);

    @PatchMapping("/{productId}/deactivate")
    @Operation(summary = "Deactivate product", description = "Mark a product as inactive")
    public ResponseEntity<ApiResponse<Void>> deactivateProduct(

            @Parameter(description = "Product ID")
            @PathVariable Long productId);

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product", description = "Delete a product by ID (soft delete recommended)")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(

            @Parameter(description = "Product ID")
            @PathVariable Long productId);

    // Get product by category:
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category", description = "Fetch products by category with pagination and status filter")
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
            @RequestParam(defaultValue = "asc") String sortDir,

            @Parameter(description = "Filter by status: all, active, inactive")
            @RequestParam(defaultValue = "all") String status);

    @GetMapping("/brand/{brandId}")
    @Operation(summary = "Get products by brand", description = "Fetch products by brand with pagination and status filter")
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
            @RequestParam(defaultValue = "asc") String sortDir,

            @Parameter(description = "Filter by status: all, active, inactive")
            @RequestParam(defaultValue = "all") String status);
}