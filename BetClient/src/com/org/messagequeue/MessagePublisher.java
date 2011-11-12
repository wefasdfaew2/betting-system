package com.org.messagequeue;

import javax.jms.JMSException;

public class MessagePublisher extends TopicPublisher implements Runnable {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public MessagePublisher(String topicname) throws JMSException {
		super(topicname);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.sendMessage(this.text);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
