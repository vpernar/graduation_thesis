package rs.raf.orderservice.eventhandlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rs.raf.events.inventory.InventoryEvent;
import rs.raf.events.payment.PaymentEvent;
import rs.raf.orderservice.service.OrderStatusUpdateEventHandler;


@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final OrderStatusUpdateEventHandler eventHandler;

    @RabbitListener(queues = "payment-event")
    public synchronized void orderUpdateEventListenerFromPayment(PaymentEvent paymentEvent) {
        log.info("Received event {}", paymentEvent);
        eventHandler.updateOrder(paymentEvent.getPayment().getOrderId(), paymentEvent.getPaymentStatus());
    }

    @RabbitListener(queues = "inventory-event")
    public synchronized void orderUpdateEventListenerFromInventory(InventoryEvent inventoryEvent) {
        log.info("Received event {}", inventoryEvent);
        eventHandler.updateOrder(inventoryEvent.getInventory().getOrderId(), inventoryEvent.getStatus());
    }

}
