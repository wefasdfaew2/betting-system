package com.org.messagequeue;

import java.util.List;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import com.org.odd.Odd;

/**
 * Use in conjunction with TopicListener to test the performance of ActiveMQ
 * Topics.
 */
public class TopicPublisher {

	private Connection connection;
	private Session session;
	private MessageProducer publisher;
	private Topic topic;
	private String url = "tcp://localhost:61616";

	@Test
	public void unittest() throws Exception {
		TopicPublisher p = new TopicPublisher();
		p.sendMessage("fuck you dog");
	}

	public TopicPublisher() throws JMSException {
		super();
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = session.createTopic("topictest.messages");
		publisher = session.createProducer(topic);
		publisher.setDeliveryMode(DeliveryMode.PERSISTENT);
		connection.start();
	}

	public void sendMessage(String mess) throws JMSException {
		publisher.send(session.createTextMessage(mess));
	}

	public void sendMapMessage(List<Odd> odds, String username)
			throws JMSException {

		for (Odd o : odds) {
			ObjectMessage mess = session.createObjectMessage(o);			
			mess.setStringProperty("username", username);
			publisher.send(mess);
		}

	}

	public void disconnect() throws JMSException {
		connection.stop();
		connection.close();
	}

}
