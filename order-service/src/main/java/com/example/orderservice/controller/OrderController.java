package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    @GetMapping("/health-check")
    public String healthCheck(HttpServletRequest request) {
        return "It's working in service order-service on port=" + request.getServerPort();
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder order) {
        order.setUserId(userId);
        OrderDto dto = orderService.createOrder(OrderDto.of(order));
        kafkaProducer.send("topic1", dto);
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
