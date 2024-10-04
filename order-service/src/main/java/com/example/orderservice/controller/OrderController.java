package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health-check")
    public String healthCheck(HttpServletRequest request) {
        return "It's working in service order-service on port=" + request.getServerPort();
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder order) {
        order.setUserId(userId);
        // JPA
//        OrderDto dto = orderService.createOrder(OrderDto.of(order));

        // Kafka
        OrderDto dto = OrderDto.of(order);
        dto.setOrderId(UUID.randomUUID().toString());
        dto.setTotalPrice(dto.getQty() * dto.getUnitPrice());

        kafkaProducer.send("example_catalog_topic", dto);
        orderProducer.send("orders", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderDto.toResponse(dto));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrdersByUserId(userId)
                        .stream().map(OrderDto::toResponse)
                        .toList());
    }

}
