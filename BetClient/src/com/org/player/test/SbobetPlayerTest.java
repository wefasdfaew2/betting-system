package com.org.player.test;

import org.junit.Test;

import com.org.odd.OddSide;
import com.org.player.SbobetPlayer;

public class SbobetPlayerTest {
	@Test
	public void testSboHomePage() {
		while (true) {
			SbobetPlayer client = null;
			SbobetPlayer client1 = null;
			SbobetPlayer player = null;
			OddSide side = OddSide.LIVE;
			try {
				client = new SbobetPlayer("maj3168200", "aaaa1111",
						side, true);
				client1 = new SbobetPlayer("Maj3259004", "aaaa1111",
						side, false);
				// player can listen to bet
				player = new SbobetPlayer("Maj3259005", "aaaa1111",
						side, false);

				player.start();
				client.start();
				// client1.start();

				player.join();
				client.join();
				// client1.join();

				Thread.sleep(5000);
			} catch (Exception e) {
				client.getLogger().error(SbobetPlayer.getStackTrace(e));
			}
		}
	}
}
