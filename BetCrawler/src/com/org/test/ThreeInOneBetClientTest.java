package com.org.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.org.client.SbobetAgentClient;
import com.org.client.ThreeInOneBetClient;

public class ThreeInOneBetClientTest {
	@Test
	public void testTestLogin3in1Bet() {
		ThreeInOneBetClient client = new ThreeInOneBetClient("lvmml7003028",
				"aq1111");
		try {
			client.testLogin3in1Bet();
		} catch (Exception e) {
			client.getLogger().error(e);
			e.printStackTrace();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
