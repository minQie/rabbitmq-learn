package priv.wmc.rabbitmq.model4;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import priv.wmc.rabbitmq.model1.connutil.RabbitMQUtil;

/**
 * 消息生产者
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:38:49
 */
@Slf4j
public class MessageProducer {

    private String producerName;
    private String exchangeName;

    private Connection connection;
    private Channel channel;

    MessageProducer(String producerName, String exchangeName, BuiltinExchangeType exchangeType) {
        init(producerName, exchangeName, exchangeType);
    }

    @SneakyThrows
    private void init(String producerName, String exchangeName, BuiltinExchangeType exchangeType) {
        this.producerName = producerName;
        this.exchangeName = exchangeName;

        connection = RabbitMQUtil.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, exchangeType);
    }

    @SneakyThrows
    public void sendMessage(String message, String routingKey) {
        // 指定路由发布消息
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        log.info(" [Producer {}] Sent '{}' to '{}'", producerName, message, routingKey);
    }

    public void close() {
        RabbitMQUtil.close(connection);
    }

}
