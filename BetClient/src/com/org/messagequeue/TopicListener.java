package com.org.messagequeue;

import java.util.HashMap;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.org.odd.Odd;

/**
 * Use in conjunction with TopicPublisher to test the performance of ActiveMQ
 * Topics.
 */
public class TopicListener implements MessageListener {
	private final Logger logger;
	private Connection connection;
	private Session session;
	private Topic topic;
	private String url = "tcp://localhost:61616";
	private HashMap<String, Odd> best_home;
	private HashMap<String, Odd> best_aways;

	public TopicListener() {
		System.setProperty("filename", "listenner_log.log");
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(TopicListener.class);
		best_aways = new HashMap<String, Odd>();
		best_home = new HashMap<String, Odd>();
	}

	public static void main(String[] argv) throws Exception {
		TopicListener l = new TopicListener();
		l.run();
	}

	public void run() throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = session.createTopic("topictest.messages");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(this);
		connection.start();
		System.out.println("Waiting for messages...");
	}

	public void processOdd(Odd odd){
		// push
	}
	public void onMessage(Message message) {
		try {
			// System.out.println(((TextMessage) message).getText());
			if(message instanceof ObjectMessage)
			{
				ObjectMessage mes = (ObjectMessage) message;
				Odd o = (Odd) mes.getObject();
				logger.info(mes.getStringProperty("username"));
				logger.info(o);
			}else if (message instanceof TextMessage) {
				TextMessage mes = (TextMessage) message;
				logger.info(mes.getText());
			}
			// logger.info(((ObjectMessage)message));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
