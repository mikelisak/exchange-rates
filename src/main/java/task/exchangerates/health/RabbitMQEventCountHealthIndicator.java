package task.exchangerates.health;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import task.exchangerates.service.NbpApiService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static java.lang.String.format;

@Component
public class RabbitMQEventCountHealthIndicator implements HealthIndicator {
    private static final Logger logger = LoggerFactory.getLogger(NbpApiService.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventCountHealthIndicator(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Override
    public Health health() {
        try {
            int eventCount = getMessageCount();
            return Health.up().withDetail("eventCount", eventCount).build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
    public int getMessageCount() {
        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
        try {
            // Get the message count from the declared queue
            int messageCount = channel.queueDeclarePassive("rate").getMessageCount();
            return messageCount;
        } catch (IOException e) {
            logger.error(format("IOException -> %s", e.getMessage()));
        } finally {
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                logger.error(format("Exception -> %s", e.getMessage()));
            }
        }
        return -1;
    }
}
