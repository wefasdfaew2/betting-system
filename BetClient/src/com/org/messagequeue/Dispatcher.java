package com.org.messagequeue;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;

public class Dispatcher implements Runnable {
	private TopicPublisher[] sbo_player;
	private TopicPublisher[] three_in_player;
	private long delay;
	private boolean isSbo;
	private boolean isThree;

	public Dispatcher(boolean isSbo, boolean isThree) {
		super();
		this.isSbo = isSbo;
		this.isThree = isThree;
	}

	@Override
	public void run() {
		try {
			String acc_content = IOUtils.toString(new FileInputStream(
					"account.txt"));
			String[] list = acc_content.split("\r\n");
			if (this.isSbo)
				this.delay = Long.parseLong(list[2].trim());
			if (this.isThree)
				this.delay = Long.parseLong(list[3].trim());

			String[] sbo = list[0].split(",");
			String[] three_in = list[1].split(",");
			sbo_player = new TopicPublisher[sbo.length];
			three_in_player = new TopicPublisher[three_in.length];
			for (int i = 0; i < sbo.length; i++) {
				sbo_player[i] = new TopicPublisher(sbo[i]);
			}
			for (int i = 0; i < three_in.length; i++) {
				three_in_player[i] = new TopicPublisher(three_in[i]);
			}
			long sbo_sleep_time = this.delay / sbo.length;
			long three_sleep_time = this.delay / three_in.length;
			while (true) {
				if (this.isSbo) {
					for (int i = 0; i < sbo.length; i++) {
						this.sbo_player[i].sendMessage("UPDATE");
						Thread.sleep(sbo_sleep_time);
					}
				} else if (this.isThree) {
					for (int i = 0; i < three_in.length; i++) {
						this.three_in_player[i].sendMessage("UPDATE");
						Thread.sleep(three_sleep_time);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
