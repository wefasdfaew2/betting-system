package com.org.player;

import com.org.messagequeue.TopicPublisher;

public class DummyPlayer {
	public static void main(String[] args) {
		try {
			TopicPublisher p = new TopicPublisher();
			while (true) {
				p.sendMessage("1");
				Thread.sleep(1000 * 60 *3);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
