package com.ecom.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.ecom.dto.ProductRequest;
import com.ecom.dto.ProductResponse;
import com.ecom.exception.ResourceNotFoundException;
import com.ecom.exception.ResourceStateException;
import com.ecom.mapper.ProductMapper;
import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductAdminServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private ProductAdminServiceImpl productAdminService;

	@Test
	void shouldCreateProduct() {

		// Arrange (Given)
		ProductRequest request = ProductRequest.builder().name("Test Product").description("This is dummy description!")
				.categoryId(1L).brandId(1L).actualPrice(BigDecimal.valueOf(999))
				.discountedPrice(BigDecimal.valueOf(888)).status(true).build();

		Product product = new Product();
		product.setName("Test Product");

		Product savedProduct = new Product();
		savedProduct.setId(1L);
		savedProduct.setName("Test Product");

		ProductResponse productResponse = ProductResponse.builder().name("Test Product").build();

		// Mock behavior
		when(productMapper.toEntity(request)).thenReturn(product);
		when(productRepository.save(product)).thenReturn(savedProduct);
		when(productMapper.toDTO(savedProduct)).thenReturn(productResponse);

		// Act (When)
		ProductResponse result = productAdminService.createProduct(request);

		// Assert (Then)
		assertEquals("Test Product", result.getName());

		// Verify interaction (important)
		verify(productRepository).save(product);
	}

	@Test
	void shouldCallFindAll_whenStatusIsAll() {

		// Arrange
		when(productRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

		// Act
		productAdminService.getAllProducts(0, 10, "name", "asc", "all");

		// Assert
		verify(productRepository).findAll(any(Pageable.class));
	}

	@Test
	void shouldUpdateProduct_whenProductExists() {

		Long productId = 1L;

		ProductRequest request = ProductRequest.builder().name("Updated Product").actualPrice(BigDecimal.valueOf(1000))
				.discountedPrice(BigDecimal.valueOf(900)).description("Updated Desc").brandId(2L).categoryId(3L)
				.status(true).build();

		Product existingProduct = new Product();
		existingProduct.setId(productId);
		existingProduct.setName("Old Product");

		Product savedProduct = new Product();
		savedProduct.setId(productId);
		savedProduct.setName("Updated Product");

		ProductResponse response = ProductResponse.builder().id(productId).name("Updated Product").build();

		// ✅ FIX: mock findById
		when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

		when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

		when(productMapper.toDTO(savedProduct)).thenReturn(response);

		// ACT
		ProductResponse result = productAdminService.updateProduct(productId, request);

		// ASSERT
		assertEquals("Updated Product", result.getName());

		// VERIFY
		verify(productRepository).save(existingProduct);
	}

	@Test
	void shouldThrowException_whenProductNotFound_forUpdate() {
		Long productId = 1L;

//    	Given:
		ProductRequest request = ProductRequest.builder().name("Updated Product").actualPrice(BigDecimal.valueOf(1000))
				.discountedPrice(BigDecimal.valueOf(900)).description("Updated Desc").brandId(2L).categoryId(3L)
				.status(true).build();

//    	Mock: Product not found:
		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
				() -> productAdminService.updateProduct(productId, request));

//    	Assert:
		assertEquals("Product not found with id : [" + productId + "]", ex.getMessage());

		verify(productRepository).findById(productId);
		verify(productRepository, never()).save(any(Product.class));
	}

	@Test
	void shouldDeleteProduct_whenProductExists() {
		Long productId = 1L;

		Product existingProduct = Product.builder().id(productId).name("Old Product").build();

//    	Mock
		when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

//    	Act
		productAdminService.deleteProduct(productId);

//    	Assert:
		verify(productRepository).findById(productId);
		verify(productRepository).deleteById(productId);
	}

	@Test
	void shouldThrowException_whenProductNotFound() {
		Long productId = 1L;

		// Mock
		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
				() -> productAdminService.deleteProduct(productId));

		assertEquals("Product not found with id : [" + productId + "]", ex.getMessage());

		verify(productRepository, never()).deleteById(productId);
	}

	@Test
	void shouldActivateProduct_whenProductIsInactive() {
		Long productId = 1L;

		Product existingProduct = Product.builder().id(productId).status(false).build();

//    	Mock:
		when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

//    	Act:
		productAdminService.activateProduct(productId);

		assertTrue(existingProduct.getStatus());
		verify(productRepository).save(existingProduct);
	}

	@Test
	void shouldThrowException_whenProductAlreadyActive() {
		Long productId = 1L;

		Product product = Product.builder().id(productId).status(true).build();

//    	Mock:
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		ResourceStateException ex = assertThrows(ResourceStateException.class,
				() -> productAdminService.activateProduct(productId));

		// Assert:

		assertEquals("Product already activated!", ex.getMessage());

		verify(productRepository).findById(productId);
		verify(productRepository, never()).save(any(Product.class));

	}

	@Test
	void shouldDeActivateProduct_whenProductIsActive() {
		Long productId = 1L;

		Product product = Product.builder().id(productId).status(true).build();

//		Mock:
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

//		Act:
		productAdminService.deactivateProduct(productId);

//		Assert:
		assertFalse(product.getStatus());
		verify(productRepository).findById(productId);
		verify(productRepository).save(product);
	}

	@Test
	void shouldThrowException_whenProductAlreadyDeActive() {
		Long productId = 1L;

		Product product = Product.builder().id(productId).status(false).build();

//		Mock: find by productId
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		ResourceStateException ex = assertThrows(ResourceStateException.class,
				() -> productAdminService.deactivateProduct(productId));

		assertEquals("Product already deactivated!", ex.getMessage());
		verify(productRepository).findById(productId);
		verify(productRepository, never()).save(any(Product.class));
	}

	@ParameterizedTest
	@CsvSource({
	    "active",
	    "inactive",
	    "all"
	})
	void shouldReturnProductsByCategory_basedOnStatus(String status) {
		Long categoryId = 1L;
		Page<Product> page = new PageImpl<>(List.of(new Product()));
		
		when(productMapper.toDTO(any(Product.class))).thenReturn(ProductResponse.builder().build());
	
		if(status.equals("active"))
			when(productRepository.findByCategoryIdAndStatusTrue(eq(categoryId), any(Pageable.class))).thenReturn(page);
		else if(status.equals("inactive"))
			when(productRepository.findByCategoryIdAndStatusFalse(eq(categoryId), any(Pageable.class))).thenReturn(page);
		else 
			when(productRepository.findByCategoryId(eq(categoryId), any(Pageable.class))).thenReturn(page);
		
		productAdminService.getProductsByCategory(categoryId, 0, 10, "id", "asc", status);
		
//		Verify based on flow:
		if(status.equals("active"))
			verify(productRepository).findByCategoryIdAndStatusTrue(eq(categoryId), any(Pageable.class));
		else if(status.equals("inactive"))
			verify(productRepository).findByCategoryIdAndStatusFalse(eq(categoryId), any(Pageable.class));
		else 
			verify(productRepository).findByCategoryId(eq(categoryId), any(Pageable.class));
	}
	
	@ParameterizedTest
	@CsvSource({
	    "active",
	    "inactive",
	    "all"
	})
	void shouldReturnProductsByBrand_basedOnStatus(String status) {
		Long brandId = 1L;
		Page<Product> page = new PageImpl<>(List.of(new Product()));
		
		when(productMapper.toDTO(any(Product.class))).thenReturn(ProductResponse.builder().build());
	
		if(status.equals("active"))
			when(productRepository.findByBrandIdAndStatusTrue(eq(brandId), any(Pageable.class))).thenReturn(page);
		else if(status.equals("inactive"))
			when(productRepository.findByBrandIdAndStatusFalse(eq(brandId), any(Pageable.class))).thenReturn(page);
		else 
			when(productRepository.findByBrandId(eq(brandId), any(Pageable.class))).thenReturn(page);
		
		productAdminService.getProductsByBrand(brandId, 0, 10, "id", "asc", status);
		
//		Verify based on flow:
		if(status.equals("active"))
			verify(productRepository).findByBrandIdAndStatusTrue(eq(brandId), any(Pageable.class));
		else if(status.equals("inactive"))
			verify(productRepository).findByBrandIdAndStatusFalse(eq(brandId), any(Pageable.class));
		else 
			verify(productRepository).findByBrandId(eq(brandId), any(Pageable.class));
	}

}
