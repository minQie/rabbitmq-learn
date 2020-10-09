package priv.wmc.rabbitmq.model4;

import com.rabbitmq.client.BuiltinExchangeType;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

/**
 * 基于官方模型四：Routing-Direct
 * 官网：https://www.rabbitmq.com/tutorials/tutorial-four-java.html
 *
 * @author Wang Mincong
 * @date 2020-09-19 16:39:44
 */
public class RabbitMQTest {

    private static final String EXCHANGE_NAME = "logs_direct";
    private static final BuiltinExchangeType EXCHANGE_TYPE = BuiltinExchangeType.DIRECT;

    @Test
    public void sendMessageTest() {
        MessageProducer producer = new MessageProducer("生产者01", EXCHANGE_NAME, EXCHANGE_TYPE);
        String logLevel = "ERROR";
        producer.sendMessage(String.format("[%s] 我是日志", logLevel), logLevel);
        producer.close();
    }

    public static void main(String[] args) {
        new MessageConsumer("消费者01", EXCHANGE_NAME, EXCHANGE_TYPE, Collections.singletonList("ERROR"))
            .startAndWaitingForMessageToConsume();
        new MessageConsumer("消费者02", EXCHANGE_NAME, EXCHANGE_TYPE, Arrays.asList("INFO", "WARN", "ERROR"))
            .startAndWaitingForMessageToConsume();
    }

}
