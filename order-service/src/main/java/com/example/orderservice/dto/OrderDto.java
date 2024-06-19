package com.example.orderservice.dto;

import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderDto implements Serializable {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;

    public static OrderDto of(OrderEntity entity) {
        return OrderDto.builder()
                .productId(entity.getProductId())
                .qty(entity.getQty())
                .unitPrice(entity.getUnitPrice())
                .totalPrice(entity.getTotalPrice())
                .orderId(entity.getOrderId())
                .userId(entity.getUserId())
                .build();
    }

    public static OrderDto of(RequestOrder request) {
        return OrderDto.builder()
                .productId(request.getProductId())
                .qty(request.getQty())
                .unitPrice(request.getUnitPrice())
                .totalPrice(request.getTotalPrice())
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .build();
    }

    public static ResponseOrder toResponse(OrderDto dto) {
        return ResponseOrder.builder()
                .orderId(dto.getOrderId())
                .productId(dto.getProductId())
                .qty(dto.getQty())
                .unitPrice(dto.getUnitPrice())
                .totalPrice(dto.getTotalPrice())
                .userId(dto.getUserId())
                .build();
    }

    public static OrderEntity toEntity(OrderDto dto) {
        return OrderEntity.builder()
                .orderId(dto.getOrderId())
                .productId(dto.getProductId())
                .qty(dto.getQty())
                .unitPrice(dto.getUnitPrice())
                .totalPrice(dto.getTotalPrice())
                .userId(dto.getUserId())
                .build();
    }
}
