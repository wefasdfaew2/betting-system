package com.org.test;

import org.junit.Test;

import com.org.client.SbobetAgentClient;

public class SbobetAgentClientTest {
	@Test
	public void testSboAgent() {
		SbobetAgentClient client = new SbobetAgentClient();
		client.testLoginSbobetAgent();
	}

}
