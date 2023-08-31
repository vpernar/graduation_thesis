package rs.raf.orchestrationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.raf.dto.InventoryRequestDto;
import rs.raf.dto.OrchestratorRequestDto;
import rs.raf.dto.OrchestratorResponseDto;
import rs.raf.dto.PaymentRequestDto;
import rs.raf.enums.OrderStatus;
import rs.raf.orchestrationservice.clients.InventoryClient;
import rs.raf.orchestrationservice.clients.PaymentClient;
import rs.raf.orchestrationservice.eventhandler.OrderEventSender;
import rs.raf.orchestrationservice.service.steps.InventoryStep;
import rs.raf.orchestrationservice.service.steps.PaymentStep;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrchestratorService {

    @Value("${rabbit.exchange}")
    private String exchange;
    @Value("${rabbit.routing-key}")
    private String routingKey;

    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;
    private final OrderEventSender orderEventSender;

    public void orderProduct(final OrchestratorRequestDto orchestratorRequestDto) {
        Workflow orderWorkflow = getOrderWorkflow(orchestratorRequestDto);

        boolean anyStepFailed = orderWorkflow.getSteps().stream()
                .anyMatch(step -> !step.process());


        OrchestratorResponseDto orchestratorResponseDto;
        if (anyStepFailed) {
            log.info("Reverting order: {}", orchestratorRequestDto);
            orchestratorResponseDto = revertOrder(orderWorkflow, orchestratorRequestDto);
        } else {
            log.info("Resolved order: {}", orchestratorRequestDto);
            orchestratorResponseDto = getResponseDto(orchestratorRequestDto, OrderStatus.ORDER_COMPLETED);
        }

        orderEventSender.emitEvent(orchestratorResponseDto, exchange, routingKey);
    }

    private OrchestratorResponseDto revertOrder(final Workflow workflow, final OrchestratorRequestDto orchestratorRequestDto) {
        List<WorkflowStep> completedSteps = workflow.getSteps().stream()
                .filter(workflowStep -> workflowStep.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .toList();

        for (WorkflowStep step : completedSteps) {
            step.revert();
        }

        return getResponseDto(orchestratorRequestDto, OrderStatus.ORDER_CANCELLED);
    }

    private Workflow getOrderWorkflow(OrchestratorRequestDto orchestratorRequestDto) {
        WorkflowStep paymentStep = new PaymentStep(paymentClient, getPaymentRequestDto(orchestratorRequestDto));
        WorkflowStep inventoryStep = new InventoryStep(inventoryClient, getInventoryRequestDto(orchestratorRequestDto));
        return new OrderWorkFlow(List.of(paymentStep, inventoryStep));
    }

    private OrchestratorResponseDto getResponseDto(OrchestratorRequestDto orchestratorRequestDto, OrderStatus status) {
        return OrchestratorResponseDto.builder()
                .orderId(orchestratorRequestDto.getOrderId())
                .amount(orchestratorRequestDto.getAmount())
                .productId(orchestratorRequestDto.getProductId())
                .userId(orchestratorRequestDto.getUserId())
                .status(status)
                .build();
    }

    private PaymentRequestDto getPaymentRequestDto(OrchestratorRequestDto orchestratorRequestDto) {
        return PaymentRequestDto.builder()
                .userId(orchestratorRequestDto.getUserId())
                .amount(orchestratorRequestDto.getAmount())
                .orderId(orchestratorRequestDto.getOrderId())
                .build();
    }

    private InventoryRequestDto getInventoryRequestDto(OrchestratorRequestDto orchestratorRequestDto) {
        return InventoryRequestDto.builder()
                .userId(orchestratorRequestDto.getUserId())
                .productId(orchestratorRequestDto.getProductId())
                .orderId(orchestratorRequestDto.getOrderId())
                .build();
    }

}
