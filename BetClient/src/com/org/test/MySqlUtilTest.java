package com.org.test;

import java.sql.SQLException;

import org.junit.Test;

import com.org.webbrowser.MySqlUtil;


public class MySqlUtilTest {
	@Test
	public void testInsert() throws SQLException{
		MySqlUtil util = new MySqlUtil("root", "123456", "jdbc:mysql://localhost/ibet");
		util.insert("hzw0801000", "1", "55", "56", "238","10/20/2011");
	}

}
