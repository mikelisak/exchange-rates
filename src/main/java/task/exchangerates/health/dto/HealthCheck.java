package task.exchangerates.health.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

@Builder
@ToString
@Data
public class HealthCheck {

    private final Status appHealth;
    private final Health rabbitMQHealth;
}
