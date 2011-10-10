package com.org.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.org.client.SbobetMemberClient;
import com.org.client.ThreeInOneBetClient;

public class SbobetMemberClientTest {

	@Test
	public void testTestLogInSbobet() {
		SbobetMemberClient client = new SbobetMemberClient();
		client.testLogInSbobet();
	}

}
