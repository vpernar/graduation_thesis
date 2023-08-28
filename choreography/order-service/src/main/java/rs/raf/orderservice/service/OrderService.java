package rs.raf.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.dto.OrderRequestDto;
import rs.raf.events.order.OrderStatus;
import rs.raf.orderservice.entity.PurchaseOrder;
import rs.raf.orderservice.exception.NotFoundException;
import rs.raf.orderservice.repository.ProductPriceRepository;
import rs.raf.orderservice.repository.PurchaseOrderRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final ProductPriceRepository productPriceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderStatusPublisher orderStatusPublisher;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.save(dtoToEntity(orderRequestDTO));

        log.info("Sending create order event: {}", purchaseOrder);
        orderStatusPublisher.raiseOrderEvent(purchaseOrder, OrderStatus.ORDER_CREATED);
        return purchaseOrder;
    }

    @Transactional
    public List<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }


    private PurchaseOrder dtoToEntity(final OrderRequestDto orderRequestDto) {
        int price = productPriceRepository.findById(orderRequestDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"))
                .getPrice();

        return PurchaseOrder.builder()
                .id(orderRequestDto.getOrderId())
                .productId(orderRequestDto.getProductId())
                .userId(orderRequestDto.getUserId())
                .orderStatus(OrderStatus.ORDER_CREATED)
                .price(price)
                .build();
    }

}
