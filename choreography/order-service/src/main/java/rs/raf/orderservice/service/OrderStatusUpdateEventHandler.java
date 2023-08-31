package rs.raf.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.events.inventory.InventoryStatus;
import rs.raf.events.order.OrderStatus;
import rs.raf.events.payment.PaymentStatus;
import rs.raf.orderservice.entity.PurchaseOrder;
import rs.raf.orderservice.repository.PurchaseOrderRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStatusUpdateEventHandler {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderStatusPublisher orderStatusPublisher;


    @Transactional
    public void updateOrder(final UUID id, Object status) {
        purchaseOrderRepository
                .findById(id)
                .ifPresent(purchaseOrder -> updateOrder(purchaseOrder, status));

    }

    private void updateOrder(PurchaseOrder purchaseOrder, Object status) {
        if (status instanceof InventoryStatus) {
            purchaseOrder.setInventoryStatus((InventoryStatus) status);
        } else {
            purchaseOrder.setPaymentStatus((PaymentStatus) status);
        }

        log.info("Updated purchase order status: {}", ((Enum<?>) status).name());
        purchaseOrderRepository.save(purchaseOrder);

        if (Objects.isNull(purchaseOrder.getInventoryStatus()) || Objects.isNull(purchaseOrder.getPaymentStatus()))
            return;

        log.info("Resolved purchase order: {}", purchaseOrder);


        boolean isComplete = PaymentStatus.RESERVED.equals(purchaseOrder.getPaymentStatus()) &&
                InventoryStatus.RESERVED.equals(purchaseOrder.getInventoryStatus());

        OrderStatus orderStatus = isComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;

        purchaseOrder.setOrderStatus(orderStatus);
        purchaseOrderRepository.save(purchaseOrder);

        if (!isComplete) {
            log.info("Purchase order is not complete: {}", purchaseOrder);
            orderStatusPublisher.raiseOrderEvent(purchaseOrder, orderStatus);
        }
    }

}
