package com.ecom.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductResponse {

	private Long id;

	private String name;

	private String description;

	private Long categoryId;

	private Long brandId;

	private BigDecimal actualPrice;

	private BigDecimal discountedPrice;

	private Boolean status;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
	
	private Long createdBy;
	
	private Long updatedBy;

}
