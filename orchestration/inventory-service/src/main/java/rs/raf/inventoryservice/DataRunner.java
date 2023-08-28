package rs.raf.inventoryservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.inventoryservice.model.Inventory;
import rs.raf.inventoryservice.repository.InventoryRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataRunner implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        Inventory inventory1 = new Inventory(1, 5);
        Inventory inventory2 = new Inventory(2, 5);
        Inventory inventory3 = new Inventory(3, 5);
        inventoryRepository.saveAll(List.of(inventory1, inventory2, inventory3));
    }
}
