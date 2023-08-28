package rs.raf.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.raf.dto.*;
import rs.raf.enums.OrderStatus;
import rs.raf.orderservice.entity.PurchaseOrder;
import rs.raf.orderservice.eventhandlers.OrderEventSender;
import rs.raf.orderservice.exception.NotFoundException;
import rs.raf.orderservice.repository.ProductPriceRepository;
import rs.raf.orderservice.repository.PurchaseOrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    @Value("${rabbit.exchange}")
    private String exchange;
    @Value("${rabbit.routing-key}")
    private String routingKey;

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductPriceRepository productPriceRepository;
    private final OrderEventSender orderEventSender;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDto) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.save(dtoToEntity(orderRequestDto));
        orderRequestDto.setOrderId(purchaseOrder.getId());

        orderEventSender.emitEvent(
                getOrchestratorRequestDTO(orderRequestDto),
                exchange,
                routingKey
        );

        return purchaseOrder;
    }

    public List<OrderResponseDto> getAll() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::entityToDto)
                .toList();
    }

    private double getProductPrice(OrderRequestDto orderRequestDto) {
        return productPriceRepository.findById(orderRequestDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"))
                .getPrice();
    }

    private PurchaseOrder dtoToEntity(final OrderRequestDto orderRequestDto) {
        return PurchaseOrder.builder()
                .id(orderRequestDto.getOrderId())
                .productId(orderRequestDto.getProductId())
                .userId(orderRequestDto.getUserId())
                .status(OrderStatus.ORDER_CREATED)
                .price(getProductPrice(orderRequestDto))
                .build();
    }

    private OrderResponseDto entityToDto(final PurchaseOrder purchaseOrder) {
        return OrderResponseDto.builder()
                .orderId(purchaseOrder.getId())
                .productId(purchaseOrder.getProductId())
                .userId(purchaseOrder.getUserId())
                .status(purchaseOrder.getStatus())
                .amount(purchaseOrder.getPrice())
                .build();
    }

    public OrchestratorRequestDto getOrchestratorRequestDTO(OrderRequestDto orderRequestDto) {
        return OrchestratorRequestDto.builder()
                .userId(orderRequestDto.getUserId())
                .amount(getProductPrice(orderRequestDto))
                .orderId(orderRequestDto.getOrderId())
                .productId(orderRequestDto.getProductId())
                .build();
    }

}
