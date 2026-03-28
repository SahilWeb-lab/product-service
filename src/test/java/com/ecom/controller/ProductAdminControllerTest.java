package com.ecom.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ecom.dto.ProductRequest;
import com.ecom.dto.ProductResponse;
import com.ecom.service.ProductAdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.payload.PageResponse;

@WebMvcTest(ProductAdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductAdminService productAdminService;

    @MockBean(name = "auditorAware")
    private AuditorAware<String> auditorAware;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateProduct() throws Exception {

        ProductRequest request = ProductRequest.builder()
                .name("iPhone")
                .description("This is dummy description!")
                .brandId(1L)
                .categoryId(1L)
                .actualPrice(BigDecimal.valueOf(1299))
                .discountedPrice(BigDecimal.valueOf(999))
                .status(true)
                .brandId(1L)
                .build();

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("iPhone")
                .build();

        when(productAdminService.createProduct(any(ProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/admin/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L));
    }
    
    @Test
    void shouldGetAllProducts() throws Exception {
    	PageResponse<ProductResponse> pageResponse = new PageResponse<>();
    	pageResponse.setContent(List.of(ProductResponse.builder()
    			.id(1L)
    			.name("Test Product")
    			.build()));
    	
    	when(productAdminService.getAllProducts(0, 10, "id", "asc", "all")).thenReturn(pageResponse);
    	
    	mockMvc.perform(get("/api/v1/admin/products"))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.data.content[0].id").value(1L));
    }
    
    @Test
    void shouldUpdateProduct() throws JsonProcessingException, Exception {
    	Long productId = 1L;
    	
    	ProductRequest productRequest = ProductRequest.builder()
    			.name("Updated Product")
    			.description("this is something!")
    			.brandId(1L)
    			.categoryId(1L)
    			.actualPrice(BigDecimal.valueOf(999))
    			.discountedPrice(BigDecimal.valueOf(880))
    			.status(true)
    			.build();
    	
    	ProductResponse productResponse = ProductResponse.builder()
    			.id(productId)
    			.name("Updated Product")
    			.build();
    	
    	when(productAdminService.updateProduct(eq(productId), any(ProductRequest.class))).thenReturn(productResponse);
    	
    	mockMvc.perform(
    			put("/api/v1/admin/products/1")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(productRequest))
    			).andExpect(status().isOk())
    			.andExpect(jsonPath("$.data.name").value("Updated Product"));
    }
    
    @Test
    void shouldActivateProduct() throws Exception {
    	Long productId = 1L;
    	
    	doNothing().when(productAdminService).activateProduct(productId);
    	mockMvc.perform(patch("/api/v1/admin/products/1/activate")).andExpect(status().isOk())
    	.andExpect(jsonPath("$.message").value("Product activated successfully!"));
    	
    }
    
    @Test
    void shouldDeactivateProduct() throws Exception {
    	Long productId = 1L;
    	
    	doNothing().when(productAdminService).activateProduct(productId);
    	mockMvc.perform(patch("/api/v1/admin/products/1/deactivate")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Product deactivated successfully!"));
    }
    
    @Test
    void shouldDeleteProduct() throws Exception {
    	Long productId = 1L;
    	
    	doNothing().when(productAdminService).deleteProduct(productId);
    	mockMvc.perform(delete("/api/v1/admin/products/1")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Product deleted successfully!"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"all", "active", "inactive"})
    void shouldGetProductsByCategory_withDifferentStatus(String productStatus) throws Exception {

        Long categoryId = 1L;

        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(List.of(
                ProductResponse.builder()
                        .id(1L)
                        .name("Test Product")
                        .build()
        ));

        when(productAdminService.getProductsByCategory(
                eq(categoryId), eq(0), eq(10), eq("id"), eq("asc"), eq(productStatus)
        )).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/admin/products/category/{categoryId}", categoryId)
                .param("status", productStatus))   
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"all", "active", "inactive"})
    void shouldGetProductsByBrand_withDifferentStatus(String productStatus) throws Exception {
    	Long brandId = 1L;
    	
    	PageResponse<ProductResponse> pageResponse = new PageResponse<>();
    	pageResponse.setContent(List.of(ProductResponse.builder()
    			.id(1L)
    			.name("Test Product")
    			.build()));
    	
    	when(productAdminService.getProductsByBrand(brandId, 0, 10, "id", "asc", productStatus)).thenReturn(pageResponse);
    	
    	mockMvc.perform(
    			get("/api/v1/admin/products/brand/{brandId}", brandId)
    			.param("status", productStatus)
    			)
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.data.content[0].id").value(1L));
    }
    
}