package com.org.test;

import org.junit.Test;

import com.org.webbrowser.ThreeInOneMemberClient;

public class ThreeInOneMemberClientTest {

	@Test
	public void testHomePage() {
		while (true) {
			ThreeInOneMemberClient client = null;
			try {
				client = new ThreeInOneMemberClient("lvmml7003028", "aq1111");

				client.start();
				client.join();
				Thread.sleep(5000);
				return;
			} catch (Exception e) {
				client.getLogger().error(e);
			}
		}
	}

}
