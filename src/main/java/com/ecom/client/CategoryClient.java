package com.ecom.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecom.client.dto.CategoryResponse;
import com.micro.payload.ApiResponse;

@FeignClient(name = "category-service")
public interface CategoryClient {
	
	@GetMapping("/api/v1/categories/{id}")
	ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id);
	
}
