package rs.raf.orchestrationservice.service.steps;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import rs.raf.dto.InventoryRequestDto;
import rs.raf.dto.InventoryResponseDto;
import rs.raf.enums.InventoryStatus;
import rs.raf.orchestrationservice.clients.InventoryClient;
import rs.raf.orchestrationservice.service.WorkflowStep;
import rs.raf.orchestrationservice.service.WorkflowStepStatus;

@RequiredArgsConstructor
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
        InventoryResponseDto inventoryResponseDto = inventoryClient.deduct(inventoryRequestDto).getBody();
        boolean operation = inventoryResponseDto.getStatus().equals(InventoryStatus.AVAILABLE);
        this.stepStatus = operation ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED;
        return operation;
    }

    @Override
    public boolean revert() {
        HttpStatusCode statusCode = inventoryClient.add(inventoryRequestDto).getStatusCode();
        return statusCode.is2xxSuccessful();
    }
}
