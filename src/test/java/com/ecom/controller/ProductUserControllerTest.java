package com.ecom.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ecom.dto.ProductFilterRequest;
import com.ecom.dto.ProductResponse;
import com.ecom.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.payload.PageResponse;

@WebMvcTest(ProductUserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean(name = "auditorAware")
    private AuditorAware<String> auditorAware;

    @Autowired
    private ObjectMapper objectMapper;

    // ==========================================
    // 1. GET PRODUCT BY ID
    // ==========================================
    @Test
    void shouldGetProductById() throws Exception {

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("iPhone")
                .build();

        when(productService.getProductById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/user/products/{productId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.message").value("Product fetched successfully!"));
    }

    // ==========================================
    // 2. GET ACTIVE PRODUCTS
    // ==========================================
    @Test
    void shouldGetActiveProducts() throws Exception {

        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(List.of(
                ProductResponse.builder().id(1L).name("iPhone").build()
        ));

        when(productService.getAllActiveProducts(0, 10, "id", "asc"))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/user/products/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L));
    }

    // ==========================================
    // 3. GET PRODUCTS BY BRAND
    // ==========================================
    @Test
    void shouldGetProductsByBrand() throws Exception {

        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(List.of(
                ProductResponse.builder().id(1L).name("iPhone").build()
        ));

        when(productService.getProductsByBrand(1L, 0, 10, "id", "asc"))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/user/products/brand/{brandId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L));
    }

    // ==========================================
    // 4. GET PRODUCTS BY CATEGORY
    // ==========================================
    @Test
    void shouldGetProductsByCategory() throws Exception {

        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(List.of(
                ProductResponse.builder().id(1L).name("iPhone").build()
        ));

        when(productService.getProductsByCategory(1L, 0, 10, "id", "asc"))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/user/products/category/{categoryId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L));
    }

    // ==========================================
    // 5. FILTER PRODUCTS (POST)
    // ==========================================
    @Test
    void shouldGetFilteredProducts() throws Exception {

        ProductFilterRequest request = ProductFilterRequest.builder()
                .keyword("iphone")
                .categoryId(1L)
                .brandId(1L)
                .minPrice(999.00)
                .maxPrice(888.00)
                .build();

        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(List.of(
                ProductResponse.builder().id(1L).name("iPhone").build()
        ));

        when(productService.getFilteredProducts(any(), eq(0), eq(10), eq("id"), eq("asc")))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/user/products/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L));
    }
}