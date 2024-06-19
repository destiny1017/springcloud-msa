package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());
        OrderEntity orderEntity = orderRepository.save(OrderDto.toEntity(orderDto));
        return OrderDto.of(orderEntity);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        return OrderDto.of(orderRepository
                            .findByOrderId(orderId)
                            .orElseThrow(() -> new EntityNotFoundException()));
    }

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId)
                .stream().map(OrderDto::of)
                .toList();
    }
}
