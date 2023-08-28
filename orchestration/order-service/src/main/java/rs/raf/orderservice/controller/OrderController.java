package rs.raf.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.dto.OrderRequestDto;
import rs.raf.dto.OrderResponseDto;
import rs.raf.orderservice.entity.PurchaseOrder;
import rs.raf.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("create")
    public ResponseEntity<PurchaseOrder> createOrder(@RequestBody OrderRequestDto orderRequestDto){
        return ResponseEntity.ok(orderService.createOrder(orderRequestDto));
    }

    @GetMapping("all")
    public ResponseEntity<List<OrderResponseDto>> getOrders(){
        return ResponseEntity.ok(orderService.getAll());
    }

}
