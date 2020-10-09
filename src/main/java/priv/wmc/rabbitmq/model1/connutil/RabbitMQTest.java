package priv.wmc.rabbitmq.model1.connutil;

import org.junit.Test;

/**
 * 基于官方模型一
 * 简单将获取连接、关闭连接的代码抽取到工具类型中，作为静态方法提供
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:39:44
 */
public class RabbitMQTest {

    @Test
    public void sendMessageTest() {
        // 生产者
        MessageProducer producer = new MessageProducer();
        producer.sendMessage("hello world!");
    }

    public static void main(String[] args) {
        // 消费者
        new MessageConsumer().startAndWaitingForMessageToConsume();
    }

}
