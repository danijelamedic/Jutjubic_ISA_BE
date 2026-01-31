package jutjubic.isa.backend.messaging.publisher;

import jutjubic.isa.backend.messaging.config.RabbitMQConfig;
import jutjubic.isa.backend.messaging.dto.UploadCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UploadEventPublisher {

    private static final String ROUTING_KEY_JSON = "upload.created";

    private final RabbitTemplate rabbitTemplate;

    public UploadEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUploadCreated(UploadCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.UPLOAD_EXCHANGE,
                ROUTING_KEY_JSON,
                event
        );
    }
}
