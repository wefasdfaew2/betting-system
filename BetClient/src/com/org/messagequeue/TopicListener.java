package com.org.messagequeue;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
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
import com.org.odd.OddType;
import com.org.webbrowser.ThreeInOneMemberClient;

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
	private String topicname;

	public TopicListener(String topicname) {
		System.setProperty("filename", "listenner_log.log");
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(TopicListener.class);
		this.engine = new OddEngine(this.logger);
		this.topicname = topicname;
	}

	public static void main(String[] argv) throws Exception {
		TopicListener l = new TopicListener("topictest.messages");
		l.run();
	}

	public void run() throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = session.createTopic(this.topicname);
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(this);
		connection.start();
		logger.info("Waiting for messages...");
	}

	private void processOdd(HashMap<String, Odd> odds, String client_name) {
		// process new odd
		try {
			this.engine.addOdd(odds, client_name);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			logger.error(ThreeInOneMemberClient.getStackTrace(e));
		}
	}

	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		try {
			// System.out.println(((TextMessage) message).getText());
			if (message instanceof ObjectMessage) {
				ObjectMessage mes = (ObjectMessage) message;
				HashMap<String, Odd> odds = (HashMap<String, Odd>) mes
						.getObject();
				logOddtable(odds, message.getStringProperty("username"));
//				this.processOdd(odds, message.getStringProperty("username"));
			} else if (message instanceof TextMessage) {
				TextMessage mes = (TextMessage) message;
				logger.info(mes.getText());
				try {
					int p = Integer.parseInt(mes.getText());
					this.engine.setPlayed(p);
					logger.info("play again " + p + "time");
				} catch (Exception e) {

				}
			}
			// logger.info(((ObjectMessage)message));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void logOddtable(HashMap<String, Odd> odds, String client) {
		for (Entry<String, Odd> e : odds.entrySet()) {
			// if (e.getValue().getHome().equals("Barcelona U19".toUpperCase()))
			logger.info(client + ":" + e.getValue());
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
