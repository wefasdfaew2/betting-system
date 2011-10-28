package com.org.webbrowser;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MySqlUtil {
	private Connection conn = null;
	private final Logger logger;
	public static final String create_table = "CREATE  TABLE `user` (  `ip1` INT NOT NULL , `ip2` INT NOT NULL ,	  `ip3` INT NOT NULL ,  `ip4` INT NOT NULL ,  `username` VARCHAR(45) NOT NULL ,	`date` DATE NOT NULL,   PRIMARY KEY (`ip1`,`ip2`,`ip3`,`ip4`,`username`,`date`)  );";

	public void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				logger.info("Database connection terminated");
			} catch (Exception e) { /* ignore close errors */
			}
		}

	}

	public MySqlUtil(String userName, String password, String url) {
		super();
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(MySqlUtil.class);
		try {		
	
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, userName, password);
			DatabaseMetaData dbm = conn.getMetaData();
			// check if "employee" table is there
			ResultSet tables = dbm.getTables(null, null, "user", null);
			if (tables.next()) {
				// Table exists
				logger.info("table exist");
			} else {
				// Table does not exist
				// then create table
				Statement create_tble = conn.createStatement();
				try {
					create_tble.executeUpdate(create_table);
				} catch (SQLException e) {
					e.printStackTrace();
					logger.error("Cannot create table");
				}
				logger.info("table not exist");
			}

			logger.info("Database connection established");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insert(String username, String ip1, String ip2, String ip3,
			String ip4, String date) throws SQLException {
		String insert_sql = "INSERT INTO user (`ip1`, `ip2`, `ip3`, `ip4`, `date`, `username`) VALUES ('"
				+ ip1
				+ "', '"
				+ ip2
				+ "', '"
				+ ip3
				+ "', '"
				+ ip4
				+ "',"
				+ "STR_TO_DATE('"
				+ date
				+ "','%m/%d/%Y')"
				+ ", '"
				+ username
				+ "');";
		String select_sql = "Select * from user where username='" + username
				+ "' and ip1='" + ip1 + "' and ip2='" + ip2 + "' and ip3='"
				+ ip3 + "' and ip4 ='" + ip4 + "' and date =" + "STR_TO_DATE('"
				+ date + "','%m/%d/%Y');";
//		System.out.println(insert_sql);
//		System.out.println(select_sql);
		Statement stm = conn.createStatement();
		ResultSet result = stm.executeQuery(select_sql);
		if (result.next()) {
			// if exist record then return and do nothing
			return;
		}

		stm.executeUpdate(insert_sql);
	}

	public void testConnection() {

	}
}
