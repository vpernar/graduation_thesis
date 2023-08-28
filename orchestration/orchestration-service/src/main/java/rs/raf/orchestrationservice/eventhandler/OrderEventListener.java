package rs.raf.orchestrationservice.eventhandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rs.raf.orchestrationservice.service.OrchestratorService;
import rs.raf.dto.OrchestratorRequestDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final OrchestratorService orchestratorService;

    @RabbitListener(queues = "order-created")
    public void orderUpdateEventListener(OrchestratorRequestDto orchestratorRequestDto) {
        log.info("Received event {}", orchestratorRequestDto);
        orchestratorService.orderProduct(orchestratorRequestDto);
    }

}
