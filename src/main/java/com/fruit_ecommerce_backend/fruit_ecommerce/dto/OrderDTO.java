package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private List<OrderItemDTO> items;
    private double total;

    @Data
    public static class OrderItemDTO {
        private Long productId;
        private int qty;
    }
}
