package priv.wmc.rabbitmq.model3;

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
        /*
        将通道绑定到指定名称和类型的交换机上（不存在则创建）

        exchange
            交换机名称
        type
            DIRECT("direct"), FANOUT("fanout"), TOPIC("topic"), HEADERS("headers")
         */
        channel.exchangeDeclare(exchangeName, exchangeType);
        // 注意，这里不能声明队列，因为生产者是直接和交换机交互，如果声明了，就和消费者中临时队列的命名不匹配，消费者就无法获取消息了
    }

    @SneakyThrows
    public void sendMessage(String message) {
        // 指定交换机发送消息
        channel.basicPublish(exchangeName, "", null, message.getBytes());
        log.info(" [Producer {}] Sent '{}'", producerName, message);
    }

    public void close() {
        RabbitMQUtil.close(connection);
    }

}
