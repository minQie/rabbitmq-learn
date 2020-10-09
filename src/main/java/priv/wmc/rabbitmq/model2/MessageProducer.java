package priv.wmc.rabbitmq.model2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import priv.wmc.rabbitmq.model1.connutil.RabbitMQUtil;

/**
 * 消息生产者（面向对象的重构优化）
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:38:49
 */
@Slf4j
public class MessageProducer {

    private String producerName;
    private Connection connection;
    private Channel channel;

    MessageProducer(String producerName, String queueName) {
        init(producerName, queueName);
    }

    @SneakyThrows
    private void init(String producerName, String queueName) {
        this.producerName = producerName;
        this.connection = RabbitMQUtil.newConnection();
        this.channel = connection.createChannel();
        this.channel.queueDeclare(queueName, false, false, false, null);
    }

    @SneakyThrows
    public void sendMessage(String message) {
        channel.basicPublish("", "", null, message.getBytes());
        log.info(" [Producer {}] Sent '{}'", producerName, message);
    }

    public void close() {
        RabbitMQUtil.close(connection);
    }

}
