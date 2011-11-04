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
			SbobetPlayer client1 = null;
			SbobetPlayer player = null;
			try {
				client = new SbobetPlayer("maj3168200", "aaaa1111",
						OddSide.TODAY, true);
				client1 = new SbobetPlayer("maj3168201", "aaaa1111",
						OddSide.TODAY, true);
				// player can listen to bet
				player = new SbobetPlayer("Maj3259005", "aaaa1111",
						OddSide.TODAY, false);

				player.start();
				client.start();
				client1.start();
				
				player.join();
				client.join();
				client1.join();

				Thread.sleep(5000);
			} catch (Exception e) {
				client.getLogger().error(SbobetPlayer.getStackTrace(e));
			}
		}
	}
}
