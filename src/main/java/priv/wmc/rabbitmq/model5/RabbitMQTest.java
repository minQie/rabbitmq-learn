package priv.wmc.rabbitmq.model5;

import com.rabbitmq.client.BuiltinExchangeType;
import java.util.Arrays;
import org.junit.Test;

/**
 * 基于官方模型五：Routing-Topic
 * 官网：https://www.rabbitmq.com/tutorials/tutorial-five-java.html
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:39:44
 */
public class RabbitMQTest {

    private static final String EXCHANGE_NAME = "exchange_topic";
    private static final BuiltinExchangeType EXCHANGE_TYPE = BuiltinExchangeType.TOPIC;

    @Test
    public void sendMessageTest() {
        MessageProducer producer = new MessageProducer("生产者01", EXCHANGE_NAME, EXCHANGE_TYPE);
        producer.sendMessage("balabala...", "user.a.b");
        producer.close();
    }

    public static void main(String[] args) {
        new MessageConsumer("消费者01", EXCHANGE_NAME, EXCHANGE_TYPE, Arrays.asList("user", "user.*"))
            .startAndWaitingForMessageToConsume();
        new MessageConsumer("消费者02", EXCHANGE_NAME, EXCHANGE_TYPE, Arrays.asList("user.#"))
            .startAndWaitingForMessageToConsume();
    }

}
