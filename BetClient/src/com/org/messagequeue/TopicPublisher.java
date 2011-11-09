package com.org.messagequeue;

import java.util.HashMap;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import com.org.odd.Odd;
import com.org.odd.TeamType;

/**
 * Use in conjunction with TopicListener to test the performance of ActiveMQ
 * Topics.
 */
public class TopicPublisher {

	private Connection connection;
	private Session session;
	private MessageProducer publisher;
	private Queue topic;
	private String url = JMSConfiguration.getHostURL();

	@Test
	public void unittest() throws Exception {
		TopicPublisher p = new TopicPublisher();
		p.sendMessage("fuck you dog");
	}

	public TopicPublisher() throws JMSException {
		super();
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		factory.setUseAsyncSend(true);
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = session.createQueue("topictest.messages");
		publisher = session.createProducer(topic);
		publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		connection.start();
	}

	public TopicPublisher(String topicname) throws JMSException {
		super();
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		factory.setUseAsyncSend(true);
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = session.createQueue(topicname);
		publisher = session.createProducer(topic);
		publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		connection.start();
	}

	public void sendMessage(String mess) throws JMSException {
		publisher.send(session.createTextMessage(mess));
	}

	public void sendMapMessage(HashMap<String, Odd> odds, String username)
			throws JMSException {
		ObjectMessage m = session.createObjectMessage(odds);
		m.setStringProperty("username", username);
		publisher.send(m);
	}

	public void sendOddMessage(Odd o, TeamType type) throws JMSException {
		ObjectMessage m = session.createObjectMessage(o);
		if (type == TeamType.HOME)
			m.setBooleanProperty("home", true);
		else if (type == TeamType.AWAY)
			m.setBooleanProperty("home", false);
		publisher.send(m);
	}

	public void disconnect() throws JMSException {
		connection.stop();
		connection.close();
	}

}
