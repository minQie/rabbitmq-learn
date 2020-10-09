package priv.wmc.rabbitmq.model4;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import priv.wmc.rabbitmq.model1.connutil.RabbitMQUtil;

/**
 * 消息消费者
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
    private DefaultConsumer defaultConsumer;

    MessageConsumer(String consumerName, String exchangeName, BuiltinExchangeType exchangeType, List<String> routingKeyList) {
        init(consumerName, exchangeName, exchangeType, routingKeyList);
    }

    @SneakyThrows
    private void init(String consumerName, String exchangeName, BuiltinExchangeType exchangeType, List<String> routingKeyList) {
        this.consumerName = consumerName;

        connection = RabbitMQUtil.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, exchangeType);
        queueName = channel.queueDeclare().getQueue();

        // 将临时队列绑定到交换机上的多个路由上
        for (String routingKey : routingKeyList) {
            channel.queueBind(queueName, exchangeName, routingKey);
        }

        defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                throws IOException
            {
                String message = new String(body, StandardCharsets.UTF_8);
                log.info(" [Consumer {}] Received '{}'", consumerName, message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
    }

    @SneakyThrows
    public void startAndWaitingForMessageToConsume() {
        channel.basicQos(1);
        channel.basicConsume(queueName, false, defaultConsumer);
        log.info(" [Consumer {}] Waiting for messages. To exit press CTRL+C", consumerName);
    }

}
