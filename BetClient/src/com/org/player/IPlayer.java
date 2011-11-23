package com.org.player;

import java.util.HashMap;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import com.org.odd.OddElement;

public interface IPlayer {
	public void homePage() throws Exception;

	public void doPolling();

	public void sendData(HashMap<String, OddElement> map_odds)
			throws JMSException;

	public void startConnection() throws JMSException;

	public HashMap<String, OddElement> getCurrent_map_odds();
	
	public Logger getLogger();

}
