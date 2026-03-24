package com.ecom.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {

	@NotBlank(message = "Product name must not be blank")
    @Size(min = 3, max = 150, message = "Product name must be between 3 and 150 characters")
	private String name;

	@NotBlank(message = "Description must not be blank")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be a positive number")
    private Long categoryId;

    @NotNull(message = "Brand ID is required")
    @Positive(message = "Brand ID must be a positive number")
    private Long brandId;

    @NotNull(message = "Actual price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Actual price must be greater than 0")
    private BigDecimal actualPrice;

    @NotNull(message = "Discounted price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discounted price must be greater than 0")
    private BigDecimal discountedPrice;

    @NotNull(message = "Status is required")
    private Boolean status;

}
