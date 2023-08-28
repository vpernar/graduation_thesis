package rs.raf.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.inventoryservice.entity.OrderInventory;

@Repository
public interface OrderInventoryRepository extends JpaRepository<OrderInventory, Integer> {
}
