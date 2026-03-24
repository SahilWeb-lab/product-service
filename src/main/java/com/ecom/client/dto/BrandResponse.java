package com.ecom.client.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandResponse {

	private Long id;
	
	private String name;
	
	private String description;
	
	private String logoUrl;
	
	private Boolean status;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

}
