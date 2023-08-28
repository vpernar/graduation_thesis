package rs.raf.paymentservice.eventhandlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.raf.events.inventory.InventoryEvent;
import rs.raf.events.order.OrderEvent;
import rs.raf.events.order.OrderStatus;
import rs.raf.events.payment.PaymentEvent;
import rs.raf.paymentservice.service.PaymentService;


@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    @Value("${rabbit.exchange}")
    private String exchange;
    @Value("${rabbit.routing-key}")
    private String routingKey;

    private final PaymentService paymentService;
    private final PaymentEventSender paymentEventSender;

    @RabbitListener(queues = "order-event-payment")
    public void orderEventListener(OrderEvent orderEvent) {
        log.info("Received event {}", orderEvent);
        processPayment(orderEvent);
    }

    private void processPayment(OrderEvent orderEvent) {
        if (orderEvent.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
            log.info("Creating order: {}", orderEvent);
            PaymentEvent paymentEvent = paymentService.newOrderEvent(orderEvent);

            log.info("Sending inventory event: {}", paymentEvent);
            paymentEventSender.emitEvent(paymentEvent, exchange, routingKey);
        } else {
            log.info("Canceling order: {}", orderEvent);
            paymentService.cancelOrderEvent(orderEvent);
        }
    }
}
