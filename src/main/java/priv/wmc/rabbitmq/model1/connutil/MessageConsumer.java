package priv.wmc.rabbitmq.model1.connutil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费者
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:38:59
 */
@Slf4j
public class MessageConsumer {

    @SneakyThrows
    public void startAndWaitingForMessageToConsume() {
        Connection connection = RabbitMQUtil.newConnection();
        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info(" [Consumer] Received '" + message + "'");
        };

        channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, false, false, false, null);
        channel.basicConsume(RabbitMQUtil.QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        log.info(" [Consumer] Waiting for messages. To exit press CTRL+C");
    }

}
