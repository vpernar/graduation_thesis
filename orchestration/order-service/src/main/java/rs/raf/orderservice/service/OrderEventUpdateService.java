package rs.raf.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.raf.dto.OrchestratorResponseDto;
import rs.raf.orderservice.entity.PurchaseOrder;
import rs.raf.orderservice.exception.NotFoundException;
import rs.raf.orderservice.repository.PurchaseOrderRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventUpdateService {
    private final PurchaseOrderRepository purchaseOrderRepository;

    public void updateOrder(final OrchestratorResponseDto orchestratorResponseDto) {
        try {
            PurchaseOrder purchaseOrder =
                    purchaseOrderRepository.findById(orchestratorResponseDto.getOrderId())
                            .orElseThrow(() -> new NotFoundException("Purchase order not found"));

            purchaseOrder.setStatus(orchestratorResponseDto.getStatus());
            purchaseOrderRepository.save(purchaseOrder);
            log.info("Updated order status {}", purchaseOrder);
        } catch (NotFoundException e) {
            log.error("Unable to save purchase order: {}", e.getMessage());
        }
    }

}
