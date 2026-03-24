package com.ecom.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@CreatedBy
	@Column(nullable = false)
	private Long createdBy;
	
	@LastModifiedBy
	@Column(nullable = false)
	private Long updatedBy;
	
}
