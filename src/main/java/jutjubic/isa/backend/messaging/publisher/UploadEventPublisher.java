package jutjubic.isa.backend.messaging.publisher;

import jutjubic.isa.backend.messaging.RabbitNames;
import jutjubic.isa.backend.messaging.dto.UploadCreatedEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class UploadEventPublisher {

    private final RabbitTemplate jsonRabbitTemplate;
    private final RabbitTemplate protobufRabbitTemplate;

    public UploadEventPublisher(
            @Qualifier("jsonRabbitTemplate") RabbitTemplate jsonRabbitTemplate,
            @Qualifier("protobufRabbitTemplate") RabbitTemplate protobufRabbitTemplate
    ) {
        this.jsonRabbitTemplate = jsonRabbitTemplate;
        this.protobufRabbitTemplate = protobufRabbitTemplate;
    }

    public void publishUploadCreatedJson(UploadCreatedEvent event) {
        jsonRabbitTemplate.convertAndSend(
                RabbitNames.EXCHANGE_UPLOAD,
                RabbitNames.RK_JSON,
                event
        );
    }

    public void publishUploadCreatedPb(jutjubic.upload.proto.UploadCreatedEvent eventPb) {
        byte[] bytes = eventPb.toByteArray();

        MessageProperties props = new MessageProperties();
        props.setContentType("application/x-protobuf");

        Message msg = new Message(bytes, props);

        protobufRabbitTemplate.send(
                RabbitNames.EXCHANGE_UPLOAD,
                RabbitNames.RK_PB,
                msg
        );
    }
}
