package com.org.messagequeue;

import javax.jms.JMSException;

import com.org.odd.Odd;
import com.org.odd.TeamType;

public class OddPublisher extends TopicPublisher implements Runnable {
	private Odd odd;
	private TeamType type;

	public Odd getOdd() {
		return odd;
	}

	public void setOdd(Odd odd) {
		this.odd = odd;
	}

	public TeamType getType() {
		return type;
	}

	public void setType(TeamType type) {
		this.type = type;
	}

	public OddPublisher() throws JMSException {
		super();
		// TODO Auto-generated constructor stub
	}

	public OddPublisher(String topicname) throws JMSException {
		super(topicname);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.sendOddMessage(this.odd, this.type);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
