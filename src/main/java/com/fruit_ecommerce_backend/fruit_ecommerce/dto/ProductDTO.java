package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.Data;


@Data
public class ProductDTO {
		private Long id;
		private String name;
		private String description;
		private double price;
		private int stock;
		private String imageBase64;
}