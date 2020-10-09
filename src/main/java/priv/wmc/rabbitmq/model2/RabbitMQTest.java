package priv.wmc.rabbitmq.model2;

import org.junit.Test;

/**
 * 基于官方模型二：Work Queues
 * 官网：https://www.rabbitmq.com/tutorials/tutorial-two-java.html
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:39:44
 */
public class RabbitMQTest {

    private static final String QUEUE_NAME = "queue2";

    @Test
    public void sendMessageTest() {
        MessageProducer producer = new MessageProducer("生产者01", QUEUE_NAME);
        for (int i = 0; i < 10; i++) {
            producer.sendMessage("hello world " + i);
        }
        producer.close();
    }

    public static void main(String[] args) {
        /*
        基本 测试1：两个消费者消费的消息数量相同，且间隔消费（消费者01 消费序号为奇数的消息，消费者02 消费序号为偶数的消息）

        在其中一个消费中添加线程等待语句，模拟慢消费者 测试2：
            重点1：消费的快的消费者已经全部消费完毕了（相关日志都打印全了），慢消费者还在一条一条的消费
            重点2：慢消费者还没消费完，RabbitMQ 管理页面，没能能看到本以为会有的积压的消息
                推理：两个消费者已经把相关消息获取到本地的队列中了，然后慢慢的消费
                应该是相关配置，没有进行开启消费确认的机制，即 Broker 将消息发送给消费者，就算消费者消费了
                很容易的在官方文档上找到了这一块内容的描述
            解决：
                首先将 channel.basicConsume 的 autoAck 参数设置为 false，关闭消息自动确认机制
                然后通过 channel.basicQos 设置队列默认拉取消息的数量为 1

            总结：采用这样的模式，消费者消费完当前消息才会继续从队列中拿取新的消息，从而达到能者多劳的效果

            思考：这样修改后默认的自动确认机制后，可以保证消息消费的不丢失，生产消息是否有类似的问题？
            答：生产者只要确保消息发送到队列中，所以这样的自动确认机制是没有任何问题的
         */
        new MessageConsumer("消费者01", QUEUE_NAME).startAndWaitingForMessageToConsume();
        new MessageConsumer("消费者02", QUEUE_NAME).startAndWaitingForMessageToConsume();
    }

}
