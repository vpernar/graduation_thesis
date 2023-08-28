package rs.raf.orderservice.eventhandlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rs.raf.dto.OrchestratorResponseDto;
import rs.raf.orderservice.service.OrderEventUpdateService;


@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    private final OrderEventUpdateService orderEventUpdateService;

    @RabbitListener(queues = "order-updated")
    public void orderUpdateEventListener(OrchestratorResponseDto orchestratorResponseDto) {
        log.info("Received event {}", orchestratorResponseDto);
        orderEventUpdateService.updateOrder(orchestratorResponseDto);
    }
}
