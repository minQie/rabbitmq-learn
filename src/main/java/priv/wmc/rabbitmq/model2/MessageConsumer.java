package priv.wmc.rabbitmq.model2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import priv.wmc.rabbitmq.model1.connutil.RabbitMQUtil;

/**
 * 消息消费者（面向对象的重构优化）
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:38:59
 */
@Slf4j
public class MessageConsumer {

    private String consumerName;
    private String queueName;
    private Connection connection;
    private Channel channel;
    private DeliverCallback defaultDeliverCallback;

    MessageConsumer(String consumerName, String queueName) {
        init(consumerName, queueName);
    }

    @SneakyThrows
    private void init(String consumerName, String queueName) {
        this.consumerName = consumerName;
        this.queueName = queueName;
        this.connection = RabbitMQUtil.newConnection();
        this.channel = connection.createChannel();
        this.channel.queueDeclare(queueName, false, false, false, null);

        this.defaultDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info(" [Consumer {}] Received '{}'", consumerName, message);
            // 当执行完业务逻辑确认无误后，手动发送确认消息
            // 将自动确认关闭，又不手动确认，就会在 RabbitMQ 管理页面上看到未确认的消息【超时？超时会再交给一个消费者去消费？】
            /*
            手动发送 ack 进行消息的消费确认

            delivery
                消息的唯一标识
            multiple
                是否开启多个消息同时确认？
            */
            this.channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
    }

    @SneakyThrows
    public void startAndWaitingForMessageToConsume() {
        // 每次只消费一条消息
        channel.basicQos(1);
        // autoAck：false 将默认的消息确认机制关闭
        channel.basicConsume(queueName, false, defaultDeliverCallback, consumerTag -> {});
        log.info(" [Consumer {}] Waiting for messages. To exit press CTRL+C", consumerName);
    }

    public void close() {
        RabbitMQUtil.close(connection);
    }

}
