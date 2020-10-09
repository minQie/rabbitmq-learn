package priv.wmc.rabbitmq.model3;

import com.rabbitmq.client.BuiltinExchangeType;
import org.junit.Test;

/**
 * 基于官方模型三：Publish/Subscribe
 * 官网：https://www.rabbitmq.com/tutorials/tutorial-three-java.html
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:39:44
 */
public class RabbitMQTest {

    private static final String EXCHANGE_NAME = "exchange3";
    private static final BuiltinExchangeType EXCHANGE_TYPE = BuiltinExchangeType.FANOUT;

    @Test
    public void sendMessageTest() {
        MessageProducer producer = new MessageProducer("生产者01", EXCHANGE_NAME, EXCHANGE_TYPE);
        for (int i = 0; i < 10; i++) {
            producer.sendMessage("hello world " + i);
        }
        producer.close();
    }

    public static void main(String[] args) {
        // 测试现象：一条消息会发送给所有消费者都消费一遍
        // 这里有个大坑，就是不像 1 2 模型，消费者必须先启动，生产者再消费消息
        // 推测：生产者只绑定到交换机，交换机应该不像队列那样能够存储消息
        new MessageConsumer("消费者01", EXCHANGE_NAME, EXCHANGE_TYPE).startAndWaitingForMessageToConsume();
        new MessageConsumer("消费者02", EXCHANGE_NAME, EXCHANGE_TYPE).startAndWaitingForMessageToConsume();
    }

}
