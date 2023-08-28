package rs.raf.inventoryservice.service;

import rs.raf.inventoryservice.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.raf.dto.InventoryRequestDto;
import rs.raf.dto.InventoryResponseDto;
import rs.raf.enums.InventoryStatus;
import rs.raf.inventoryservice.model.Inventory;
import rs.raf.inventoryservice.repository.InventoryRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    public InventoryResponseDto deductInventory(final InventoryRequestDto inventoryRequestDto) {
        Inventory inventory = inventoryRepository.findById(inventoryRequestDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found in inventory"));

        int quantity = inventory.getQuantity();

        InventoryStatus inventoryStatus = InventoryStatus.AVAILABLE;
        if (quantity - 1 < 0) {
            inventoryStatus = InventoryStatus.UNAVAILABLE;
        } else {
            inventory.setQuantity(quantity - 1);
            inventoryRepository.save(inventory);
        }

        log.info("Resolved deduct action for {}, with status {}", inventoryRequestDto, inventoryStatus);

        return InventoryResponseDto.builder()
                .orderId(inventoryRequestDto.getOrderId())
                .userId(inventoryRequestDto.getUserId())
                .productId(inventoryRequestDto.getProductId())
                .status(inventoryStatus)
                .build();
    }

    @Transactional
    public void addInventory(final InventoryRequestDto inventoryRequestDto) {
        Inventory inventory = inventoryRepository.findById(inventoryRequestDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found in inventory"));

        inventory.setQuantity(inventory.getQuantity() + 1);

        log.info("Resolved add action for {}", inventoryRequestDto);
    }
}
