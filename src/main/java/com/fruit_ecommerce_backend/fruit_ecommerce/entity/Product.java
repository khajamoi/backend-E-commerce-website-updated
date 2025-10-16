package com.fruit_ecommerce_backend.fruit_ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		
		private String name;
		@Column(length = 2000)
		private String description;
		private double price;
		private int stock;
		 @Lob
		 @Column(columnDefinition = "MEDIUMBLOB")  // for MySQL
		 private byte[] image;}