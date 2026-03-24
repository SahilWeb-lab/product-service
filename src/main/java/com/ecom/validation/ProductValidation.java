package com.ecom.validation;

import org.springframework.stereotype.Component;

import com.ecom.client.BrandClient;
import com.ecom.client.CategoryClient;
import com.ecom.client.dto.BrandResponse;
import com.ecom.client.dto.CategoryResponse;
import com.ecom.dto.ProductRequest;
import com.ecom.exception.ResourceNotFoundException;
import com.micro.payload.ApiResponse;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductValidation {

	private final CategoryClient categoryClient;
	private final BrandClient brandClient;

//	Validate product:
	public void validateProduct(ProductRequest productRequest) {

//		Validate brand:
		Long brandId = productRequest.getBrandId();

		try {
			ApiResponse<BrandResponse> brandById = brandClient.getBrandById(brandId);
		} catch (FeignException ex) {
			throw new ResourceNotFoundException("Brand not found with id : [" + brandId + "]");
		}

//		Validate category:
		Long categoryId = productRequest.getCategoryId();
		try {
			ApiResponse<CategoryResponse> apiResponse = categoryClient.getCategoryById(categoryId);
		} catch (FeignException ex) {

			throw new ResourceNotFoundException("Category not found with id : [" + categoryId + "]");
		}
	}

}
