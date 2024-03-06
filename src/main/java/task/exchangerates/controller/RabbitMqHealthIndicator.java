package task.exchangerates.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqHealthIndicator implements HealthIndicator {
    private final ConnectionFactory connectionFactory;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public Health health() {
        try {
            connectionFactory.createConnection().close();
            return Health.up().build();
        } catch (Exception e) {
            return Health.down().withException(e).build();
        }
    }
}
