package com.example.orderservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUserId(String userId);
    Optional<OrderEntity> findByOrderId(String orderId);
}
