package rs.raf.orchestrationservice.eventhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventSender {
    private final ObjectMapper objectMapper;
    private final AmqpTemplate rabbitTemplate;

    public void emitEvent(Object object, String exchange, String routingKey) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        try {
            log.info("Sending event to routing key: {}", routingKey);
            rabbitTemplate.send(exchange, routingKey, new Message(objectMapper.writeValueAsBytes(object), messageProperties));
        } catch (Exception e) {
            log.error("Error sending message to RabbitMQ: {}", e.getMessage(), e);
        }
    }
}
