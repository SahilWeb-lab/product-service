package com.ecom.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductFilterRequest {

	private String keyword;
	
	private Long categoryId;
	
	private Long brandId;
	
	private Double minPrice;
	
	private Double maxPrice;
	
	private Boolean status; // null = ALL

}
