package com.org.messagequeue;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;

public class Dispatcher implements Runnable {
	private TopicPublisher[] sbo_player;
	private TopicPublisher[] three_in_player;
	private long delay;
	private boolean isSbo;
	private boolean isThree;
	private String acc_file;

	public static void main(String[] args) {
		try {
			long sleep_time = Long.parseLong(args[0]);
			String acc_content = IOUtils.toString(new FileInputStream(
					args[1]));
			String[] list = acc_content.split("\r\n");

			String[] sbo = list[0].split(";");
			String[] three_in = list[1].split(";");

			ProcessBuilder[] sbo_workers = new ProcessBuilder[sbo.length];
			ProcessBuilder[] three_workers = new ProcessBuilder[three_in.length];
			// start worker
			for (int i = 0; i < three_in.length; i++) {
				String[] th = three_in[i].split(",");
				three_workers[i] = new ProcessBuilder(
						"jdk1.6.0_21\\bin\\java.exe", "-jar", "3inbet.jar",
						th[0], th[1]);
				three_workers[i].start();
				Thread.sleep(sleep_time);
			}
			for (int i = 0; i < sbo.length; i++) {
				String[] sb = sbo[i].split(",");
				sbo_workers[i] = new ProcessBuilder(
						"jdk1.6.0_21\\bin\\java.exe", "-jar", "sbobet.jar",
						sb[0], sb[1]);
				sbo_workers[i].start();
				Thread.sleep(sleep_time);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public Dispatcher(boolean isSbo, boolean isThree,String acc_file) {
		super();
		this.isSbo = isSbo;
		this.isThree = isThree;
		this.acc_file = acc_file;
	}

	@Override
	public void run() {
		try {
			String acc_content = IOUtils.toString(new FileInputStream(
					this.acc_file));
			String[] list = acc_content.split("\r\n");
			if (this.isSbo)
				this.delay = Long.parseLong(list[2].trim());
			if (this.isThree)
				this.delay = Long.parseLong(list[3].trim());

			String[] sbo = list[0].split(";");
			String[] three_in = list[1].split(";");
			this.sbo_player = new TopicPublisher[sbo.length];
			this.three_in_player = new TopicPublisher[three_in.length];
			// start worker
			for (int i = 0; i < three_in.length; i++) {
				String[] th = three_in[i].split(",");
				three_in_player[i] = new TopicPublisher(th[0]);
			}
			for (int i = 0; i < sbo.length; i++) {
				String[] sb = sbo[i].split(",");
				sbo_player[i] = new TopicPublisher(sb[0]);
			}

			long sbo_sleep_time = this.delay / sbo.length;
			long three_sleep_time = this.delay / three_in.length;

			// looping send message to worker
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
