package com.org.player.test;

import org.junit.Test;

import com.org.odd.OddSide;
import com.org.player.SbobetPlayer;
import com.org.webbrowser.SbobetMemberClient;

public class SbobetPlayerTest {
	@Test
	public void testSboHomePage() {
		while (true) {
			try {
				SbobetPlayer client = new SbobetPlayer(
						"Maj3259002", "aaaa1111", OddSide.LIVE);

				client.run();
				client.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
