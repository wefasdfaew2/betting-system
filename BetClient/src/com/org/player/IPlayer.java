package com.org.player;

import java.util.HashMap;

import javax.jms.JMSException;

import com.org.odd.OddElement;

public interface IPlayer {
	public void homePage() throws Exception;

	public void doPolling();

	public void sendData(HashMap<String, OddElement> map_odds)
			throws JMSException;

}
