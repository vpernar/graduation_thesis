package rs.raf.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.dto.InventoryDto;
import rs.raf.events.inventory.InventoryEvent;
import rs.raf.events.inventory.InventoryStatus;
import rs.raf.events.order.OrderEvent;
import rs.raf.inventoryservice.entity.OrderInventoryConsumption;
import rs.raf.inventoryservice.repository.OrderInventoryConsumptionRepository;
import rs.raf.inventoryservice.repository.OrderInventoryRepository;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final OrderInventoryRepository inventoryRepository;
    private final OrderInventoryConsumptionRepository consumptionRepository;

    @Transactional
    public InventoryEvent newOrderInventory(OrderEvent orderEvent) {
        InventoryDto dto = InventoryDto.builder()
                .orderId(orderEvent.getPurchaseOrder().getOrderId())
                .productId(orderEvent.getPurchaseOrder().getProductId())
                .build();

        return inventoryRepository.findById(orderEvent.getPurchaseOrder().getProductId())
                .filter(orderInventory -> orderInventory.getAvailableInventory() > 0)
                .map(orderInventory -> {
                    orderInventory.setAvailableInventory(orderInventory.getAvailableInventory() - 1);
                    inventoryRepository.save(orderInventory);
                    consumptionRepository.save(OrderInventoryConsumption.of(
                            orderEvent.getPurchaseOrder().getOrderId(),
                            orderEvent.getPurchaseOrder().getProductId(),
                            1
                    ));
                    return new InventoryEvent(dto, InventoryStatus.RESERVED);
                })
                .orElse(new InventoryEvent(dto, InventoryStatus.REJECTED));
    }

    @Transactional
    public void cancelOrderInventory(OrderEvent orderEvent) {
        consumptionRepository.findById(orderEvent.getPurchaseOrder().getOrderId())
                .ifPresent(orderInventoryConsumption -> {
                    inventoryRepository.findById(orderInventoryConsumption.getProductId())
                            .ifPresent(orderInventory -> {
                                orderInventory.setAvailableInventory(
                                        orderInventory.getAvailableInventory() + orderInventoryConsumption.getQuantityConsumed());
                                inventoryRepository.save(orderInventory); //?
                            });
                    consumptionRepository.delete(orderInventoryConsumption);
                });
    }

}
