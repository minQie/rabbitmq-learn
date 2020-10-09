package priv.wmc.rabbitmq.model1.connutil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息生产者
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:38:49
 */
@Slf4j
public class MessageProducer {

    @SneakyThrows
    public void sendMessage(String message) {
        Connection connection = RabbitMQUtil.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, false, false, false, null);

        channel.basicPublish("", RabbitMQUtil.QUEUE_NAME, null, message.getBytes());
        log.info(" [Producer] Sent '" + message + "'");

        RabbitMQUtil.close(connection);
    }

}
