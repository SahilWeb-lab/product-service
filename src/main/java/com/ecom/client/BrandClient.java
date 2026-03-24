package com.ecom.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecom.client.dto.BrandResponse;
import com.micro.payload.ApiResponse;

@FeignClient(name = "brand-service")
public interface BrandClient {

	@GetMapping("/api/v1/brands/{id}")
	ApiResponse<BrandResponse> getBrandById(@PathVariable Long id);
	
}
