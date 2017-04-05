package kohler.zero.activemq.p2p;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Kohler on 2017/3/22.
 */
public class Consumer {
    private ActiveMQConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public Consumer() {
        this.factory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616"
        );

        try {
            this.connection = factory.createConnection();
            connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = this.session.createQueue("first");
            this.consumer = session.createConsumer(queue, "color='blue'");
            this.consumer.setMessageListener(new Listener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void consume() {

    }

    class Listener implements MessageListener {

        @Override
        public void onMessage(Message message) {
            if (message instanceof MapMessage) {
                System.out.println(message);

                MapMessage tmp = (MapMessage) message;
                try {
                    String name = tmp.getString("name");
                    System.out.println("name: " + name);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();

        consumer.consume();
    }
}
