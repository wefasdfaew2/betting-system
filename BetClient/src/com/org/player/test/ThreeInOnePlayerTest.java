package com.org.player.test;

import org.junit.Test;

import com.org.odd.OddSide;
import com.org.player.SbobetPlayer;
import com.org.player.ThreeInOnePlayer;
import com.org.webbrowser.ThreeInOneMemberClient;

public class ThreeInOnePlayerTest {

	@Test
	public void testHomePage() {
		while (true) {
			ThreeInOnePlayer client = null;
			ThreeInOnePlayer client1 = null;
			ThreeInOnePlayer client2 = null;
			ThreeInOnePlayer client3 = null;
			try {
				client = new ThreeInOnePlayer("lvmml7006002", "bbbb1111",
						OddSide.TODAY);
				client1 = new ThreeInOnePlayer("lvmml7006003", "bbbb1111",
						OddSide.TODAY);				
				client2 = new ThreeInOnePlayer("lvmml7006004", "bbbb1111",
						OddSide.TODAY);
				client3 = new ThreeInOnePlayer("lvmml7006005", "bbbb1111",
						OddSide.TODAY);
				
				client.start();
				Thread.sleep(10000);
				client1.start();
//				Thread.sleep(10000);
//				client2.start();
//				Thread.sleep(10000);
//				client3.start();
				
				client.join();
				client1.join();
//				client2.join();
//				client3.join();
				
				Thread.sleep(5000);
			} catch (Exception e) {
				client.getLogger().error(ThreeInOnePlayer.getStackTrace(e));
			}
		}
	}

}
