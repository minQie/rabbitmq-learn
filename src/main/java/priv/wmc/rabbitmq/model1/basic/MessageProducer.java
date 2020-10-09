package priv.wmc.rabbitmq.model1.basic;

import static priv.wmc.rabbitmq.model1.basic.RabbitMQTest.QUEUE_NAME;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息生产者
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:38:49
 */
@Slf4j
@AllArgsConstructor
public class MessageProducer {

    final ConnectionFactory factory;

    @SneakyThrows
    public void sendMessage(String message) {
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            /*
            声明消息队列（没有就创建）

            queueName：
                队列名称
            durable：
                是否开启队列的持久化（注意在生产者那边的值是需要保证一致的，否则会被 RabbitMQ 当作两个不同的队列）
                false - RabbitMQ 重启队列就没了，还没有被消费的消息就没了
                true - RabbitMQ 重启队列还在，但是队列中的消息依然没有，想要消息持久化，需要在消息生产者发送消息的时候设置一个属性，详见生产者【如果队列关闭持久化，那消息的持久化是否还有用？】
            exclusive：
                是否独占队列，如果设置为 true，其他的 channel 就不能再绑定到该队列了【生产者也是？怎么区分生产者消费者？】
            autoDelete：
                是否在消费完成后自动删除队列
                注意是消费者在消费完且断开连接后，才会删除队列
            arguments：
                额外附加参数
             */
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            /*
            向指定消息队列发送消息

            exchange
                交换机名称，的确没有指定交换机，但是不指定的话，rabbitmq 会指定一个默认的交换机
            routingKey

            props
                额外设置，配置 MessageProperties.PERSISTENT_TEXT_PLAIN 才会开启消息的持久化
            body
                具体的消息内容
             */
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

            log.info(" [Producer] Sent '" + message + "'");
        }
    }

}
