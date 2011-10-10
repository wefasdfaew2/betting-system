package com.org.betcrawler;

public interface ICrawler {
	public boolean LogIn(String username, String password, String URL);
	public String GetData();
}
