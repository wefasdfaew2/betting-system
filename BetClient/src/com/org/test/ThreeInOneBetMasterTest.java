package com.org.test;

import org.junit.Test;

import com.org.odd.OddSide;
import com.org.webbrowser.AccountImporter;
import com.org.webbrowser.ThreeInOneMemberClient;

public class ThreeInOneBetMasterTest {
	@Test
	public void unittest() {
		AccountImporter master = new AccountImporter(
				"E:\\Dev\\acc\\lvmml7003000-027_new.txt");
		int num_thread = 5;
		try {
			while (true) {
				int i = 0;
				ThreeInOneMemberClient[] clients = new ThreeInOneMemberClient[num_thread];

				for (String acc : master.parseAccount_file()) {
					clients[i] = new ThreeInOneMemberClient(acc, OddSide.LIVE);
					clients[i].start();
					Thread.sleep(5000);
					i++;
					// get only num_thread instance
					if (i > num_thread - 1)
						break;
				}
				for (ThreeInOneMemberClient client : clients) {
					client.join();
				}
				Thread.sleep(5000);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
