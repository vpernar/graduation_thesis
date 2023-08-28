package rs.raf.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.dto.OrderRequestDto;
import rs.raf.orderservice.entity.PurchaseOrder;
import rs.raf.orderservice.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("create")
    public ResponseEntity<PurchaseOrder> createOrder(@RequestBody OrderRequestDto requestDTO) {
        requestDTO.setOrderId(UUID.randomUUID());
        return ResponseEntity.ok(orderService.createOrder(requestDTO));
    }

    @GetMapping("all")
    public ResponseEntity<List<PurchaseOrder>> getOrders() {
        return ResponseEntity.ok(orderService.getAll());
    }

}
