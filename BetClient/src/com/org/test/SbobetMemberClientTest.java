package com.org.test;

import org.junit.Test;

import com.org.webbrowser.SbobetMemberClient;

public class SbobetMemberClientTest {
	@Test
	public void testSboHomePage() {
		while (true) {
			try {
				SbobetMemberClient client = new SbobetMemberClient(
						"maj3168200", "aaaa1111");

				client.run();
				client.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
