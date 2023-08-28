package rs.raf.inventoryservice.eventhandlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.raf.events.inventory.InventoryEvent;
import rs.raf.events.order.OrderEvent;
import rs.raf.events.order.OrderStatus;
import rs.raf.inventoryservice.service.InventoryService;


@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    @Value("${rabbit.exchange}")
    private String exchange;
    @Value("${rabbit.routing-key}")
    private String routingKey;

    private final InventoryService inventoryService;
    private final InventoryEventSender inventoryEventSender;

    @RabbitListener(queues = "order-event-inventory")
    public void orderEventListener(OrderEvent orderEvent) {
        log.info("Received event {}", orderEvent);
        processInventory(orderEvent);
    }

    private void processInventory(OrderEvent orderEvent) {
        if (orderEvent.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
            log.info("Creating order: {}", orderEvent);
            InventoryEvent inventoryEvent = inventoryService.newOrderInventory(orderEvent);

            log.info("Sending inventory event: {}", inventoryEvent);
            inventoryEventSender.emitEvent(inventoryEvent, exchange, routingKey);
        } else {
            log.info("Canceling order: {}", orderEvent);
            inventoryService.cancelOrderInventory(orderEvent);
        }
    }
}
