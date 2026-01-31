package jutjubic.isa.backend.messaging.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String UPLOAD_EXCHANGE = "jutjubic.upload";

    @Bean
    public TopicExchange uploadExchange() {
        return new TopicExchange(UPLOAD_EXCHANGE, true, false);
    }
}
