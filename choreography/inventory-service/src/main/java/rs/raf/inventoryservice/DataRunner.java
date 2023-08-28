package rs.raf.inventoryservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.inventoryservice.entity.OrderInventory;
import rs.raf.inventoryservice.repository.OrderInventoryRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataRunner implements CommandLineRunner {
    private final OrderInventoryRepository orderInventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        OrderInventory orderInventory1 = OrderInventory.of(1, 5);
        OrderInventory orderInventory2 = OrderInventory.of(2, 5);
        OrderInventory orderInventory3 = OrderInventory.of(3, 5);
        orderInventoryRepository.saveAll(List.of(orderInventory1, orderInventory2, orderInventory3));
    }
}
