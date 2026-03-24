package com.ecom.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecom.dto.ProductResponse;
import com.ecom.util.ApiResponseUtil;
import com.micro.payload.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(exception = ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<ProductResponse>> handleResourceNotFound(ResourceNotFoundException exception) {
		return ApiResponseUtil.error(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(exception = ResourceStateException.class)
	public ResponseEntity<ApiResponse<ProductResponse>> handleResourceState(ResourceStateException exception) {
		return ApiResponseUtil.error(exception.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<ApiResponse<ProductResponse>> handleException(Exception exception) {
		return ApiResponseUtil.error(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Object>> handleValidationException(
	        MethodArgumentNotValidException ex) {

	    Map<String, String> errors = new HashMap<>();

	    ex.getBindingResult().getFieldErrors()
	            .forEach(error ->
	                    errors.put(error.getField(), error.getDefaultMessage()));

	    ApiResponse<Object> response = ApiResponse.builder()
	            .status(false)
	            .message("Validation failed")
	            .data(errors)
	            .build();

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
}
