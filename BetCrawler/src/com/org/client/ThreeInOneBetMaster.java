package com.org.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ThreeInOneBetMaster {
	private String account_file_name;

	public String getAccount_file_name() {
		return account_file_name;
	}

	public void setAccount_file_name(String account_file_name) {
		this.account_file_name = account_file_name;
	}

	@Test
	public void parseAccount_file() throws IOException {
		InputStream ip = new FileInputStream(new File(account_file_name));
		String content = IOUtils.toString(ip, "utf-8");
		System.out.println(content);
	}

}
