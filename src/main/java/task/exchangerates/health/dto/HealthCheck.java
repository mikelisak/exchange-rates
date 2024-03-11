package task.exchangerates.health.dto;

import lombok.*;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

@Builder
@ToString
@Data
public class HealthCheck {

    private Status appHealth;
    private Health rabbitMQHealth;
}
