package com.org.webbrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class AccountImporter {
	private String account_file_name;

	public String getAccount_file_name() {
		return account_file_name;
	}

	public void setAccount_file_name(String account_file_name) {
		this.account_file_name = account_file_name;
	}

	public AccountImporter(String account_file_name) {
		super();
		this.account_file_name = account_file_name;
	}

	public String[] parseAccount_file() throws IOException {
		InputStream ip = new FileInputStream(new File(account_file_name));
		String content = IOUtils.toString(ip, "utf-8");
		return content.split(";");
	}

}
