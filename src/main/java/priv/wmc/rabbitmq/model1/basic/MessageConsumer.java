package priv.wmc.rabbitmq.model1.basic;

import static priv.wmc.rabbitmq.model1.basic.RabbitMQTest.QUEUE_NAME;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费者
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:38:59
 */
@Slf4j
@AllArgsConstructor
public class MessageConsumer {

    final ConnectionFactory factory;

    @SneakyThrows
    public void startAndWaitingForMessageToConsume() {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info(" [Consumer] Received '" + message + "'");
        };

        /*
        生产者类中，已经对 channel.queueDeclare 方法及方法参数相关做了相关说明，这里不做赘述
        就是要强调一点，这里的参数必须要和生产者一样，否则会被 RabbitMQ 认作是两个不同的队列【应该不是所有参数都作为标识消息队列的吧？是的】
        注意：并不是说认作两个不同的队列，就会再创建一个仅该属性不同的队列，而是在调用下面语句时抛出异常
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        /*
        开启一个线程用于监听消息队列的消息，有就消费
        queueName：
            队列名称
        autoAck：
            是否开启消费者消费消息的自动确认机制
            默认行为是消费者只要拿到消息就告诉 RabbitMQ 已经消费完了，不关心你的业务逻辑有没有处理完
            RabbitMQ 了解到消费者将消息消费完了，就会将消息从消息队列中删除
            这样的机制是不能接受的，所以需要将该参数设置为 false
        deliverCallback：
            消费消息具体的逻辑
        cancelCallback：

         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        log.info(" [Consumer] Waiting for messages. To exit press CTRL+C");

        // 在这里立即关闭的话，现象可能就是消息被消费了。但是消费消息的逻辑未来得及执行，消费者线程就结束了
//        connection.close();
//        channel.close();
    }

}
