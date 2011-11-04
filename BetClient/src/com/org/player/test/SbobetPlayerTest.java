package com.org.player.test;

import org.junit.Test;

import com.org.odd.OddSide;
import com.org.player.SbobetPlayer;
import com.org.webbrowser.SbobetMemberClient;

public class SbobetPlayerTest {
	@Test
	public void testSboHomePage() {
		while (true) {
			SbobetPlayer client = null;
			try {
				client = new SbobetPlayer("Maj3259002", "aaaa1111",
						OddSide.TODAY);

				client.start();
				client.join();
				Thread.sleep(5000);
			} catch (Exception e) {
				client.getLogger().error(SbobetPlayer.getStackTrace(e));
			}
		}
	}
}
