package com.spring.rest;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Product {
	private Long id;
	@NotBlank(message = "Name cannot be blank")
	private String name;
	@Positive(message = "Price cannot be negative")
	private double price;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
