package com.org.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.org.client.SbobetAgentClient;
import com.org.client.ThreeInOneBetClient;

public class ThreeInOneBetClientTest {
	@Test
	public void testTestLogin3in1Bet() {
		ThreeInOneBetClient client = new ThreeInOneBetClient();
		client.testLogin3in1Bet();
	}

}
