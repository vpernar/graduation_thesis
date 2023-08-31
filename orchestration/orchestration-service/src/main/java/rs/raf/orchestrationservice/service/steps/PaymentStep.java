package rs.raf.orchestrationservice.service.steps;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import rs.raf.dto.PaymentRequestDto;
import rs.raf.dto.PaymentResponseDto;
import rs.raf.enums.PaymentStatus;
import rs.raf.orchestrationservice.clients.PaymentClient;
import rs.raf.orchestrationservice.service.WorkflowStep;
import rs.raf.orchestrationservice.service.WorkflowStepStatus;

@RequiredArgsConstructor
@Slf4j
public class PaymentStep implements WorkflowStep {

    private final PaymentClient paymentClient;
    private final PaymentRequestDto paymentRequestDto;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;


    @Override
    public WorkflowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public boolean process() {
        log.info("Sending request to payment-service");
        PaymentResponseDto paymentResponseDto = paymentClient.debit(paymentRequestDto).getBody();
        log.info("Received response from payment service: {}", paymentResponseDto);
        boolean operation = paymentResponseDto.getStatus().equals(PaymentStatus.PAYMENT_APPROVED);
        this.stepStatus = operation ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED;
        return operation;
    }

    @Override
    public boolean revert() {
        log.info("Sending revert request to payment-service");
        HttpStatusCode statusCode = paymentClient.credit(paymentRequestDto).getStatusCode();
        return statusCode.is2xxSuccessful();
    }

}
