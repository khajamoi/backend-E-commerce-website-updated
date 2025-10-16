package com.fruit_ecommerce_backend.fruit_ecommerce.service;

import com.fruit_ecommerce_backend.fruit_ecommerce.dto.OrderResponseDTO;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.OrderEntity;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * Place an order with items.
     */
    @Transactional
    public OrderEntity placeOrder(OrderEntity order) {
        order.setStatus(order.getStatus() == null ? "PENDING" : order.getStatus());
        return orderRepository.save(order);
    }

    /**
     * Get all orders with only the required fields for admin view.
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrdersForAdmin() {
        List<OrderEntity> orders = orderRepository.findAllWithDetails();

        return orders.stream().map(o -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(o.getId());
            dto.setStatus(o.getStatus());
            dto.setTotal(o.getTotal());
            dto.setCreatedAt(o.getCreatedAt());

            if (o.getUser() != null) {
                dto.setUserId(o.getUser().getId());
                dto.setUserName(o.getUser().getName());
                dto.setUserEmail(o.getUser().getEmail());
            }

            dto.setRazorpayPaymentId(o.getRazorpayPaymentId());

            dto.setItems(o.getItems().stream().map(i -> {
                OrderResponseDTO.OrderItemDTO itemDto = new OrderResponseDTO.OrderItemDTO();
                itemDto.setId(i.getId());
                itemDto.setProductId(i.getProduct() != null ? i.getProduct().getId() : null);
                itemDto.setProductName(i.getProduct() != null ? i.getProduct().getName() : null);
                itemDto.setPrice(i.getPrice());
                itemDto.setQuantity(i.getQuantity());
                return itemDto;
            }).collect(Collectors.toList()));

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Get a single order by ID with items and user info.
     */
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long id) {
        OrderEntity o = orderRepository.findById(id).orElseThrow();

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(o.getId());
        dto.setStatus(o.getStatus());
        dto.setTotal(o.getTotal());
        dto.setCreatedAt(o.getCreatedAt());

        if (o.getUser() != null) {
            dto.setUserName(o.getUser().getName());
            dto.setUserEmail(o.getUser().getEmail());
        }

        dto.setRazorpayPaymentId(o.getRazorpayPaymentId());

        dto.setItems(o.getItems().stream().map(i -> {
            OrderResponseDTO.OrderItemDTO itemDto = new OrderResponseDTO.OrderItemDTO();
            itemDto.setProductId(i.getProduct() != null ? i.getProduct().getId() : null);
            itemDto.setProductName(i.getProduct() != null ? i.getProduct().getName() : null);
            itemDto.setPrice(i.getPrice());
            itemDto.setQuantity(i.getQuantity());
            return itemDto;
        }).collect(Collectors.toList()));

        return dto;
    }
}
