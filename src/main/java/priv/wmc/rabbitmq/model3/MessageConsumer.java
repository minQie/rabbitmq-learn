package priv.wmc.rabbitmq.model3;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    MessageConsumer(String consumerName, String exchangeName, BuiltinExchangeType exchangeType) {
        init(consumerName, exchangeName, exchangeType);
    }

    @SneakyThrows
    private void init(String consumerName, String exchangeName, BuiltinExchangeType exchangeType) {
        this.consumerName = consumerName;

        connection = RabbitMQUtil.newConnection();
        channel = connection.createChannel();

        // 绑定交换机
        channel.exchangeDeclare(exchangeName, exchangeType);
        // 改动：调用无参构造，得到默认策略的临时队列
        queueName = channel.queueDeclare().getQueue();
        // 将临时队列绑定到交换机上【routingKey 暂时不考虑】
        channel.queueBind(queueName, exchangeName, "");

        // 修改消费逻辑定义方式
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

    /* 不定义关闭方法了 */

}
