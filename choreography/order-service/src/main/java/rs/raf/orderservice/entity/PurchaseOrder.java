package rs.raf.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import rs.raf.events.inventory.InventoryStatus;
import rs.raf.events.order.OrderStatus;
import rs.raf.events.payment.PaymentStatus;

import java.util.UUID;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    private UUID id;
    private Integer userId;
    private Integer productId;
    private Integer price;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;
}