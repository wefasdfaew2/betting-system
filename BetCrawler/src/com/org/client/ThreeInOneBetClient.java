package com.org.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.org.betcrawler.Crawler;
import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;

public class ThreeInOneBetClient {

	public void parseOddData(String element_name, String data) {
		JSONObject obj;
		try {
			obj = new JSONObject(data);
			JSONArray array = obj.getJSONArray(element_name);
			for (int i = 0; i < array.length(); i++) {
				System.out.println(array.getJSONArray(i));
			}
			System.out.println();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getDate() {
		Calendar now = Calendar.getInstance();
		return (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DATE)
				+ "/" + now.get(Calendar.YEAR) + " "
				+ (now.get(Calendar.HOUR_OF_DAY) + 1) + ":"
				+ now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND)
				+ "." + now.get(Calendar.MILLISECOND) + "0000";
	}

	public void testLogin3in1Bet() {
		Crawler crawler = new Crawler("http://www.3in1bet.com/",
				"Checker.aspx", "", "", Site.THREEINONE);
		InputStream ip = crawler.BasicGetStreamFromURL(
				"http://www.3in1bet.com/", "");
		try {
			// System.out.println("Main");
			String html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);

			ip.close();

			// System.out.println("Checker");
			ip = crawler.BasicGetStreamFromURL(
					"http://www.3in1bet.com/Checker.aspx",
					"http://www.3in1bet.com/");

			html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);
			ip.close();

			// System.out.println("Default");
			ip = crawler.BasicGetStreamFromURL(
					"http://mem52.3in1bet.com/Default.aspx",
					"http://www.3in1bet.com/");

			html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);
			ip.close();

			// System.out.println("Verify");

			String code = crawler.GetValidateCode(
					"http://mem52.3in1bet.com/verify.aspx",
					"http://mem52.3in1bet.com/Default.aspx");
			// System.out.println(code);

			List<BasicNameValuePair> post_data = new ArrayList<BasicNameValuePair>();
			post_data.add(new BasicNameValuePair("ddl_language", "en-US"));
			post_data.add(new BasicNameValuePair("UserName", "lvmml7003000"));
			post_data.add(new BasicNameValuePair("Password", "aq1111"));
			post_data.add(new BasicNameValuePair("txtInvalidation", code));

			ip = crawler.basicPost(post_data,
					"http://mem52.3in1bet.com/main/processlogin.aspx");
			html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);
			ip.close();

			ip = crawler.BasicGetStreamFromURL(
					"http://mem52.3in1bet.com/Main.aspx",
					"http://mem52.3in1bet.com/");

			html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);
			ip.close();

			// Get odds data
			System.out.println(getDate());

			String postdata = "fc=1&m_accType=MY&SystemLanguage=en-US&TimeFilter=0&m_gameType=S_&m_SortByTime=0&m_LeagueList=&SingleDouble=single&clientTime=&c=A&fav=&exlist=0";
			String postdata1 = "fc=5&m_accType=MY&SystemLanguage=en-US&TimeFilter=0&m_gameType=S_&m_SortByTime=0&m_LeagueList=&SingleDouble=single&clientTime="
					+ getDate() + "&c=A&fav=&exlist=0";
			String postdata2 = "fc=3&m_accType=MY&SystemLanguage=en-US&TimeFilter=0&m_gameType=S_&m_SortByTime=0&m_LeagueList=&SingleDouble=single&clientTime="
				+ getDate() + "&c=A&fav=&exlist=0";
			String post_url = "http://mem52.3in1bet.com/Member/BetsView/BetLight/DataOdds.ashx";

			ip = crawler.basicPost(postdata, post_url);
			html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);

			parseOddData("today", html);
			
			parseOddData("data", html);
			
			ip.close();
			
			
			ip = crawler.basicPost(postdata1, post_url);
			html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);

			parseOddData("data", html);
			ip.close();
			
			ip = crawler.basicPost(postdata2, post_url);
			html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);

			parseOddData("data", html);
			ip.close();
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("encoding error");
			// e.printStackTrace();
		}
	}

}
