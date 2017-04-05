package kohler.zero.activemq.helloworld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Kohler on 2017/3/22.
 */
public class Sender {
    public static void main(String[] args) throws JMSException, InterruptedException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616");

        Connection connection = factory.createConnection();
        connection.start();

//        1、事务，需要session手动commit；2、AUTO自动相应ack
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        Queue first = session.createQueue("first");

        MessageProducer producer = session.createProducer(null);
//        持久化
//        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

//        优先级默认为4，0-4普通，5-9加急，并不能严格保证顺序，但是加急的比普通的先到达，并不保证先消费
//        默认不过期，可以设置过期时间，过期的会放在死信里（DLQ:Dead Letter Queue）
        for (int i = 0; i < 10; i++) {
            TextMessage msg = session.createTextMessage("message content:" + i);
//            MapMessage mapMessage = session.createMapMessage();
//            mapMessage.setString("name", "z3");

            producer.send(first, msg, DeliveryMode.PERSISTENT, 4, 1000 * 10);

//            TimeUnit.SECONDS.sleep(1);
        }

//        事务需要commit
        session.commit();

        if (connection != null) {
            connection.close();
        }

    }
}
