package rs.raf.orchestrationservice.service.steps;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import rs.raf.dto.InventoryRequestDto;
import rs.raf.dto.InventoryResponseDto;
import rs.raf.enums.InventoryStatus;
import rs.raf.orchestrationservice.clients.InventoryClient;
import rs.raf.orchestrationservice.service.WorkflowStep;
import rs.raf.orchestrationservice.service.WorkflowStepStatus;

@RequiredArgsConstructor
@Slf4j
public class InventoryStep implements WorkflowStep {

    private final InventoryClient inventoryClient;
    private final InventoryRequestDto inventoryRequestDto;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    @Override
    public WorkflowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public boolean process() {
        log.info("Sending request to inventory-service");
        InventoryResponseDto inventoryResponseDto = inventoryClient.deduct(inventoryRequestDto).getBody();
        log.info("Received response from inventory service: {}", inventoryResponseDto);
        boolean operation = inventoryResponseDto.getStatus().equals(InventoryStatus.AVAILABLE);
        this.stepStatus = operation ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED;
        return operation;
    }

    @Override
    public boolean revert() {
        log.info("Sending revert request to inventory-service");
        HttpStatusCode statusCode = inventoryClient.add(inventoryRequestDto).getStatusCode();
        return statusCode.is2xxSuccessful();
    }
}
