package kohler.zero.activemq.helloworld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Kohler on 2017/3/22.
 */
public class Receiver {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616");

        Connection connection = factory.createConnection();
        connection.start();

//        1、事务，需要session手动commit；2、AUTO自动相应ack；CLIENT需要手动签收；DUPS不响应
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("first");
        MessageConsumer consumer = session.createConsumer(queue);

        while (true) {
            TextMessage msg = (TextMessage) consumer.receive();
//            msg.acknowledge(); // 手动签收，否则消息不会被消费掉
//            session.commit(); // 开启事务需要commit
            System.out.println("收到：" + msg.getText());
        }

//        connection.close();
    }
}
