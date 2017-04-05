package kohler.zero.activemq.p2p;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Kohler on 2017/3/22.
 */
public class Producer {
    private ActiveMQConnectionFactory factory;
    private Connection connection;
    private Session session;
    private Queue queue;
    private MessageProducer producer;

    public Producer() {
        this.factory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616"
        );

        try {
            this.connection = factory.createConnection();
            connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            topic发布订阅模式
//            Topic topic = session.createTopic("second");
            this.queue = session.createQueue("first");
            this.producer = session.createProducer(queue);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void produce() {
        try {
            MapMessage m1 = session.createMapMessage();
            m1.setString("name", "z3");
            m1.setInt("age", 25);
            m1.setStringProperty("color", "blue");

            MapMessage m2 = session.createMapMessage();
            m2.setString("name", "l4");
            m2.setInt("age", 20);
//            需要setStringProperty，consumer的messageSelector才能生效
            m2.setString("color", "blue");

            MapMessage m3 = session.createMapMessage();
            m3.setString("name", "w5");
            m3.setInt("age", 18);
            m3.setStringProperty("color", "red");

            producer.send(m1);
            producer.send(m2);
            producer.send(m3);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer();

        producer.produce();
        producer.close();
    }
}
