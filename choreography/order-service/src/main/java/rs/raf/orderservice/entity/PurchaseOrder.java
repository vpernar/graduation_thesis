package rs.raf.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
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
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private InventoryStatus inventoryStatus;

    @Version
    private int version;

}