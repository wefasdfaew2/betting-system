package com.org.player.test;

import org.junit.Test;

import com.org.odd.OddSide;
import com.org.player.ThreeInOnePlayer;
import com.org.webbrowser.ThreeInOneMemberClient;

public class ThreeInOnePlayerTest {

	@Test
	public void testHomePage() {
		while (true) {
			ThreeInOnePlayer client = null;
			try {
				client = new ThreeInOnePlayer("lvmml7006002", "bbbb1111",
						OddSide.LIVE);

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
