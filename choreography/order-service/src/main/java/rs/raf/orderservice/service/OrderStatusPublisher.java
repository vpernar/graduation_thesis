package rs.raf.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.raf.dto.PurchaseOrderDto;
import rs.raf.events.order.OrderEvent;
import rs.raf.events.order.OrderStatus;
import rs.raf.orderservice.entity.PurchaseOrder;
import rs.raf.orderservice.eventhandlers.OrderEventSender;

@Service
@RequiredArgsConstructor
public class OrderStatusPublisher {
    @Value("${rabbit.exchange}")
    private String exchange;
    @Value("${rabbit.routing-key}")
    private String routingKey;

    private final OrderEventSender orderEventSender;

    public void raiseOrderEvent(final PurchaseOrder purchaseOrder, OrderStatus orderStatus) {
        PurchaseOrderDto purchaseOrderDto = PurchaseOrderDto.builder()
                .orderId(purchaseOrder.getId())
                .productId(purchaseOrder.getProductId())
                .price(purchaseOrder.getPrice())
                .userId(purchaseOrder.getUserId())
                .build();

        OrderEvent orderEvent = new OrderEvent(purchaseOrderDto, orderStatus);
        orderEventSender.emitEvent(orderEvent, exchange, routingKey);
    }

}
