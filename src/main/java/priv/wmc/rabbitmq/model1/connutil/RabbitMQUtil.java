package priv.wmc.rabbitmq.model1.connutil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.SneakyThrows;

/**
 * @author Wang Mincong
 * @date 2020-09-27 18:29:40
 */
public final class RabbitMQUtil {

    private RabbitMQUtil() {}

    static final String QUEUE_NAME = "queue1";

    private static final ConnectionFactory FACTORY = new ConnectionFactory();
    static {
        FACTORY.setHost("localhost");
        FACTORY.setVirtualHost("hello");
        FACTORY.setUsername("chest");
        FACTORY.setPassword("123");
    }

    /**
     * 获取一个新连接
     * @return 新连接
     */
    public static Connection newConnection() {
        try {
            return FACTORY.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接
     * 注：关闭连接会同时把连接下的所有通道关闭
     *
     * @param connection 连接
     * @return 是否关闭成功
     */
    public static boolean close(Connection connection) {
        boolean closeSuccess = true;

        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
                closeSuccess = false;
            }
        }
        return closeSuccess;
    }

    @SneakyThrows
    public boolean close(Channel channel) {
        boolean closeSuccess = true;

        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
                closeSuccess = false;
            }
        }

        return closeSuccess;
    }

}
