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
import com.org.odd.OddEngine;

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
	private OddEngine engine;

	public TopicListener() {
		System.setProperty("filename", "listenner_log.log");
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(TopicListener.class);
		this.engine = new OddEngine(this.logger);
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

	private void processOdd(Odd odd, String client_name) {
		// process new odd
		this.engine.addOdd(odd, client_name);
	}

	public void onMessage(Message message) {
		try {
			// System.out.println(((TextMessage) message).getText());
			if (message instanceof ObjectMessage) {
				ObjectMessage mes = (ObjectMessage) message;
				Odd odd = (Odd) mes.getObject();
				// logger.info(mes.getStringProperty("username"));
				// logger.info(odd);
				processOdd(odd, mes.getStringProperty("username"));
			} else if (message instanceof TextMessage) {
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
