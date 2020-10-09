package priv.wmc.rabbitmq.model1.basic;

import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;

/**
 * 基于官方模型一
 * 官网：https://www.rabbitmq.com/tutorials/tutorial-one-java.html
 *
 * 最基础的测试
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:39:44
 */
public class RabbitMQTest {

    static final String QUEUE_NAME = "model1";

    final ConnectionFactory factory = new ConnectionFactory();

    public RabbitMQTest() {
        factory.setHost("localhost");
        factory.setVirtualHost("hello");
        factory.setUsername("chest");
        factory.setPassword("123");
    }

    @Test
    public void sendMessageTest() {
        // 向消息队列发送一条消息
        MessageProducer producer = new MessageProducer(factory);
        producer.sendMessage("hello world!");
    }

    public static void main(String[] args) {
        // 注意。下面这个不能用 junit test 来跑，因为 junit test 的设计理念是主要主线程运行结束了，子线程会全部杀死
        RabbitMQTest rabbitMQTest = new RabbitMQTest();
        new MessageConsumer(rabbitMQTest.factory).startAndWaitingForMessageToConsume();
    }

}
