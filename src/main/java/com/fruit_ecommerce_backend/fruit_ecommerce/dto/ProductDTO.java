package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String imageBase64;
    private String category;

    private Double offerPercentage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate offerStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate offerEndDate;

    private Boolean festivalOffer;
    private String festivalName;

    private Boolean offerActive;

    // final saved value (mapped to product.offerPrice / DB column offer_price)
    private Double offerPrice;
}
