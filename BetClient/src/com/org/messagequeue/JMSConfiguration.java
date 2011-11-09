package com.org.messagequeue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class JMSConfiguration {
	public static String getHostURL() {
		try {
			return IOUtils.toString(new FileInputStream("host.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return "tcp://localhost:61616?jms.useAsyncSend=true&wireFormat.maxInactivityDuration=0";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "tcp://localhost:61616?jms.useAsyncSend=true&wireFormat.maxInactivityDuration=0";
		}
	}
}
