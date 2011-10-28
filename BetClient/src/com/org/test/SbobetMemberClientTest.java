package com.org.test;

import org.junit.Test;

import com.org.webbrowser.SbobetMemberClient;

public class SbobetMemberClientTest {
	@Test
	public void testSboHomePage() {
		while (true) {
			try {
				SbobetMemberClient client = new SbobetMemberClient(
						"maj1599999", "aaaa1111");

				client.run();
				client.join();
				// return;
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
}
